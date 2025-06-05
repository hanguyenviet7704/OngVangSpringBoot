// Auth utilities
const AuthUtils = {
    // Get token from localStorage
    getToken: function() {
        return localStorage.getItem('token');
    },

    // Get user info from localStorage
    getUser: function() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    // Check if user is authenticated
    isAuthenticated: function() {
        return !!this.getToken();
    },

    // Check if user is admin
    isAdmin: function() {
        const user = this.getUser();
        return user && user.role === 'ADMIN';
    },

    // Add auth header to request
    addAuthHeader: function(headers = {}) {
        const token = this.getToken();
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }
        return headers;
    },

    // Handle unauthorized response
    handleUnauthorized: function() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
    },

    // Logout
    logout: function() {
        const token = this.getToken();
        if (token) {
            fetch('/api/auth/logout', {
                method: 'POST',
                headers: this.addAuthHeader()
            })
            .then(response => {
                if (response.ok) {
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    window.location.href = '/login?logout';
                }
            })
            .catch(error => {
                console.error('Logout failed:', error);
                this.handleUnauthorized();
            });
        } else {
            window.location.href = '/login?logout';
        }
    }
};

// Add auth interceptor to all fetch requests
const originalFetch = window.fetch;
window.fetch = function(url, options = {}) {
    // Add auth header
    options.headers = AuthUtils.addAuthHeader(options.headers || {});

    return originalFetch(url, options)
        .then(response => {
            if (response.status === 401) {
                AuthUtils.handleUnauthorized();
            }
            return response;
        });
};

// Add auth check to protected pages
document.addEventListener('DOMContentLoaded', function() {
    const protectedPaths = ['/admin', '/home', '/profile'];
    const currentPath = window.location.pathname;

    if (protectedPaths.some(path => currentPath.startsWith(path))) {
        if (!AuthUtils.isAuthenticated()) {
            window.location.href = '/login';
            return;
        }

        // Check admin access
        if (currentPath.startsWith('/admin') && !AuthUtils.isAdmin()) {
            window.location.href = '/home';
        }
    }
}); 