
    let userId ;
    function getUserID() {
    return $.ajax({
    url: "http://localhost:8081/api/users/now",
    type: "GET"
});
}
    document.addEventListener('DOMContentLoaded', function () {
    getUserID()
        .then(function(response) {
            userId = response;
            if (userId > 0) {
                loadCartData();
            } else {
                showEmptyCart();
            }
        })
        .catch(function(err) {
            console.error("Lỗi khi lấy ID user", err);
            showEmptyCart();
        });
});
    let cartData = null;
    let cartItemToDelete = null;

    // Load cart data when page loads
    document.addEventListener('DOMContentLoaded', function() {
    getUserID();
    if (userId > 0) {
    loadCartData();
} else {
    showEmptyCart();
}
});

    // Load cart data from API
    function loadCartData() {
    showLoading(true);
    hideError();

    fetch('/api/cart/' + userId)
    .then(response => {
    if (!response.ok) {
    throw new Error('Network response was not ok');
}
    return response.json();
})
    .then(data => {
    cartData = data;
    renderCartData(data);
    showLoading(false);
})
    .catch(error => {
    console.error('Error loading cart:', error);
    showError('Có lỗi xảy ra khi tải giỏ hàng. Vui lòng thử lại.');
    showLoading(false);
});
}

    // Render cart data
    function renderCartData(data) {
    const cartItemsTable = document.getElementById('cartItemsTable');
    const totalPriceElement = document.getElementById('totalPrice');

    if (!data.cartItems || data.cartItems.length === 0) {
    showEmptyCart();
    return;
}

    showCartContent();

    // Clear existing table rows
    cartItemsTable.innerHTML = '';

    // Populate table with cart items
    data.cartItems.forEach(function(item, index) {
    const row = createCartItemRow(item, index + 1);
    cartItemsTable.appendChild(row);
});

    // Update total price
    totalPriceElement.textContent = formatCurrency(data.totalAmount);
}

    // Create table row for cart item
    function createCartItemRow(item, index) {
    const row = document.createElement('tr');
    row.innerHTML =
    '<td class="table-serial"><h6>' + index + '</h6></td>' +
    '<td class="table-image">' +
    '<img src="/loadImage?imageName=' + (item.productImage || '') + '" alt="product" />' +
    '</td>' +
    '<td class="table-name"><h6>' + (item.productName || '') + '</h6></td>' +
    '<td class="table-quantity">' +
    '<div class="input-group">' +
    '<button class="decrease-quantity" type="button" onclick="decreaseQuantity(' + item.id + ')">-</button>' +
    '<input type="text" class="quantity-input" value="' + item.quantity + '" data-item-id="' + item.id + '" readonly>' +
    '<button class="increase-quantity" type="button" onclick="increaseQuantity(' + item.id + ')">+</button>' +
    '</div>' +
    '</td>' +
    '<td class="table-price">' +
    '<h6>' + formatCurrency(item.price) + '/kg</h6>' +
    '</td>' +
    '<td class="table-total">' +
    '<h6>' + formatCurrency(item.subtotal) + '</h6>' +
    '</td>' +
    '<td class="table-action">' +
    '<a class="view" href="/productDetail?id=' + item.productId + '" title="Chi tiết sản phẩm">' +
    '<i class="fas fa-eye"></i>' +
    '</a>' +
    '<a class="trash" href="javascript:void(0);" title="Xóa sản phẩm" ' +
    'onclick="showDeleteConfirmModal(' + item.id + ', \'' + (item.productName || '') + '\')">' +
    '<i class="icofont-trash"></i>' +
    '</a>' +
    '</td>';
    return row;
}

    // Increase quantity
    function increaseQuantity(cartItemId) {
    const quantityInput = document.querySelector('[data-item-id="' + cartItemId + '"]');
    const currentQuantity = parseInt(quantityInput.value);
    const newQuantity = currentQuantity + 1;

    updateCartItemQuantity(cartItemId, newQuantity);
}

    // Decrease quantity
    function decreaseQuantity(cartItemId) {
    const quantityInput = document.querySelector('[data-item-id="' + cartItemId + '"]');
    const currentQuantity = parseInt(quantityInput.value);

    if (currentQuantity > 1) {
    const newQuantity = currentQuantity - 1;
    updateCartItemQuantity(cartItemId, newQuantity);
}
}

    // Update cart item quantity via API
    function updateCartItemQuantity(cartItemId, quantity) {
    fetch('/api/cart/' + userId + '/items/' + cartItemId + '?quantity=' + quantity, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            cartData = data;
            renderCartData(data);
        })
        .catch(error => {
            console.error('Error updating quantity:', error);
            showError('Có lỗi xảy ra khi cập nhật số lượng. Vui lòng thử lại.');
        });
}

    // Show delete confirmation modal
    function showDeleteConfirmModal(cartItemId, productName) {
    cartItemToDelete = cartItemId;
    document.getElementById('productName').textContent = productName;
    $('#configmationId').modal('show');
}

    // Handle delete confirmation
    document.getElementById('yesOption').addEventListener('click', function() {
    if (cartItemToDelete) {
    removeCartItem(cartItemToDelete);
    $('#configmationId').modal('hide');
}
});

    // Remove cart item via API
    function removeCartItem(cartItemId) {
    fetch('/api/cart/' + userId + '/items/' + cartItemId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            cartData = data;
            renderCartData(data);
            cartItemToDelete = null;
        })
        .catch(error => {
            console.error('Error removing item:', error);
            showError('Có lỗi xảy ra khi xóa sản phẩm. Vui lòng thử lại.');
        });
}

    // Show/hide functions
    function showEmptyCart() {
    document.getElementById('emptyCart').style.display = 'block';
    document.getElementById('cartContent').style.display = 'none';
    document.getElementById('checkoutSection').style.display = 'none';
}

    function showCartContent() {
    document.getElementById('emptyCart').style.display = 'none';
    document.getElementById('cartContent').style.display = 'block';
    document.getElementById('checkoutSection').style.display = 'block';
}

    // Utility functions
    function formatCurrency(amount) {
    return amount.toFixed(0).replace(/\d(?=(\d{3})+$)/g, '$&,') + ' đ';
}

    function showLoading(show) {
    const loadingElement = document.getElementById('loadingSpinner');
    loadingElement.style.display = show ? 'block' : 'none';
}

    function showError(message) {
    const errorElement = document.getElementById('cartError');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}

    function hideError() {
    const errorElement = document.getElementById('cartError');
    errorElement.style.display = 'none';
}