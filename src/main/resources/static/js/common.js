// Gắn sự kiện bằng Event Delegation để tránh lỗi DOM chưa sẵn sàng
document.addEventListener('click', function(e) {
    if (e.target && e.target.id === 'logout-link') {
        e.preventDefault();
        handleLogout();
    }
});

// Hàm xử lý đăng xuất
function handleLogout() {
    fetch('/api/auth/logout', {
        method: 'POST',
        credentials: 'include'
    })
    .then(response => {
        if (response.ok) {
            localStorage.clear();
            // Chuyển hướng kèm query ?logout để hiển thị thông báo
            window.location.href = '/login?logout';
        } else {
            alert("Có lỗi xảy ra khi đăng xuất. Vui lòng thử lại.");
            window.location.href = '/login';
        }
    })
    .catch(error => {
        console.error('Logout failed:', error);
        window.location.href = '/login';
    });
}
