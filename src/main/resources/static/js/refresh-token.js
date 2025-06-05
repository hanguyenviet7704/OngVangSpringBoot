// Token refresh utilities
const TokenRefresh = {
    // Check if token is expired
    isTokenExpired: function(token) {
        if (!token) return true;
        
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            const { exp } = JSON.parse(jsonPayload);
            return exp * 1000 < Date.now();
        } catch (e) {
            return true;
        }
    },

    // Refresh token
    refreshToken: function() {
        const token = localStorage.getItem('token');
        if (!token) return Promise.reject('No token found');

        return fetch('/api/auth/refresh', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Token refresh failed');
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('token', data.token);
            return data.token;
        });
    },

    // Add token refresh to fetch interceptor
    setupRefreshInterceptor: function() {
        const originalFetch = window.fetch;
        window.fetch = async function(url, options = {}) {
            const token = localStorage.getItem('token');
            
            if (token && TokenRefresh.isTokenExpired(token)) {
                try {
                    const newToken = await TokenRefresh.refreshToken();
                    options.headers = {
                        ...options.headers,
                        'Authorization': 'Bearer ' + newToken
                    };
                } catch (error) {
                    console.error('Token refresh failed:', error);
                    AuthUtils.handleUnauthorized();
                    return Promise.reject(error);
                }
            }

            return originalFetch(url, options);
        };
    }
};

// Setup refresh interceptor
TokenRefresh.setupRefreshInterceptor(); 