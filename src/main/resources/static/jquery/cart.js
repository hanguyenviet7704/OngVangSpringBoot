function loadCart(userId) {
    $.ajax({
        url: `http://localhost:8081/api/cart/${userId}?status=1`,
        method: "GET",
        success: function (data) {

            let totalQuantity = 0;
            if (data.cartItems && data.cartItems.length > 0) {
                data.cartItems.forEach(item => {
                    totalQuantity += item.quantity;
                });
            }
            $('#totalCartItems').text(totalQuantity);


            // lấy dữ liệu để hiện ở phần giỏ hàng bên phải trang home
            let cartSidebar = $('.cart-sidebar');
            let cartList = '';

            if (data.cartItems && data.cartItems.length > 0) {
                $('.cart-total span').html(`<span>Tổng số lượng giỏ hàng (${data.cartItems.length})</span>`);
                data.cartItems.forEach(item => {
                    cartList += `
                            <li class="cart-item">
                                <div class="cart-media">
                                    <a href="/productDetail?id=${item.productId}">
                                        <img src="/loadImage?imageName=${item.productImage || 'default.jpg'}" alt="product">
                                    </a>
                                </div>
                                <div class="cart-info-group">
                                    <div class="cart-info">
                                        <h6>
                                            <label>Tên sản phẩm :</label>
                                            <a href="/productDetail?id=${item.productId}" style="color: #119744">${item.productName}</a>
                                        </h6>
                                        <label>Đơn giá :</label>
                                        <p>${item.price.toLocaleString()} đ</p>
                                    </div>
                                    <div class="cart-action-group">
                                        <div class="product-action">
                                            <label>Số lượng :</label>
                                            <input class="action-input" title="Quantity Number" type="text" name="quantity" value="${item.quantity}">
                                        </div>
                                        <h6>${item.subtotal.toLocaleString()} đ</h6>
                                    </div>
                                </div>
                            </li>
                        `;
                });

                $('.cart-list').html(cartList);
                $('.cart-footer').show();
            } else {
                $('.cart-total span').html(`<i class="fas fa-shopping-basket"></i><span>Tổng số lượng giỏ hàng (0)</span>`);
                $('.cart-list').html('');
                $('.cart-footer').hide();

                // Hiển thị thông báo trống
                $('.cart-sidebar').html(`
                        <div class="cart-header">
                            <div class="cart-total">
                                <i class="fas fa-shopping-basket"></i><span>Tổng số lượng giỏ hàng (0)</span>
                            </div>
                            <button class="cart-close"><i class="icofont-close"></i></button>
                        </div>
                        <div class="text-center">
                            <h4 style="color: #119744" class="mt-5">Hiện tại bạn chưa có sản phẩm nào trong giỏ hàng!</h4>
                            <h5 style="color: #119744">Mua sắm ngay nào!</h5>
                            <a href="/products" style="text-decoration: underline;">Click tại đây!</a>
                        </div>
                    `);
            }
        },
        error: function () {
            alert('Không thể tải giỏ hàng. Vui lòng thử lại sau!');
        }
    });
}

// Gọi hàm khi mở giỏ hàng (ví dụ)
$(document).ready(function () {
    let userId;

    function getUserID() {
        return $.ajax({
            url: "http://localhost:8081/api/users/now",
            type: "GET"
        });
    }

    getUserID()
        .then(function (response) {
            userId = response;
            loadCart(userId);
        })
        .catch(function (err) {
            console.error("Lỗi khi lấy ID user", err);
        });
});