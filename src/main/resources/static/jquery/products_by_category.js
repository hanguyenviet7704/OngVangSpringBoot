$(document).ready(function () {
    let userId;

    // 1. Kiểm tra và lấy thông tin người dùng
    function checkLogin() {
        return $.ajax({
            url: "http://localhost:8081/api/users/now",
            type: "GET"
        }).then(function(response) {
            userId = response;
            return response;
        }).catch(function() {
            userId = null;
            return null;
        });
    }

    // 2. Xác định route và load sản phẩm
    const pathSegments = window.location.pathname.split('/');
    const categoryId = pathSegments.pop() || pathSegments.pop();
    
    // Chỉ chạy khi ở route /productsByCategory/{id}
    if (pathSegments.includes('productsByCategory') && categoryId) {
        checkLogin().then(() => {
            fetchProductsByCategory(categoryId);
            initCartHandlers();
        });
    }

    // 3. Hàm thêm vào giỏ hàng
    function addToCart(productId, quantity = 1) {
        return $.ajax({
            url: `http://localhost:8081/api/cart/${userId}/items`,
            type: "POST",
            data: {
                productId: productId,
                quantity: quantity
            }
        });
    }

    // 4. Khởi tạo sự kiện thêm giỏ hàng
    function initCartHandlers() {
        $(document).on('click', '.product-add', function(e) {
            e.preventDefault();
            const productId = $(this).closest('.product-card').find('.product-name a').attr('href').split('id=')[1];

            if ($(this).find('span').text() === "Hết hàng") {
                alert("Sản phẩm đã hết hàng!");
                return;
            }



            addToCart(productId)
                .then(() => {
                    location.reload();
                })
                .catch(err => {
                    console.error("Lỗi thêm giỏ hàng:", err);

                });
        });
    }

    // 5. Fetch và render sản phẩm theo danh mục
    function fetchProductsByCategory(categoryId, page = 0, size = 12) {
        $.ajax({
            url: "http://localhost:8081/api/find/productsByCategory/",
            type: "GET",
            data: {categoryID: categoryId, page: page, size: size},
            success: function (response) {
                renderProduct(response.content);
                renderPagination(categoryId, response.totalPages, response.pageNumber);
            },
            error: function (err) {
                console.error("Lỗi khi gọi API sản phẩm theo category:", err);
            }
        });
    }

    function renderProduct(products) {
        $('#list').empty();
        products.forEach(product => {
            const discountedPrice = product.price - (product.price * product.discount / 100);
            const formatCurrency = number => number.toLocaleString('vi-VN') + ' đ';
            const stockStatus = product.quantity === 0
                ? `<span>Hết hàng</span>`
                : `<span>Thêm vào giỏ</span>`;
            const row = `
                <div class="col">
                    <div class="product-card" data-product-id="${product.productId}">
                        <div class="product-media">
                            ${product.discount > 0 ? `<div class="product-label"><label class="label-text sale">- ${product.discount}%</label></div>` : ''}
                            <button class="product-wish">
                                <i class="fas fa-heart"></i>
                            </button>
                            <a class="product-image" href="/productDetail?id=${product.productId}">
                                <img src="/loadImage?imageName=${product.productImage}" alt="${product.productName}">
                            </a>
                            <div class="product-widget">
                                <a title="Yêu thích" href="/login" class="fas fa-heart"></a>
                                <a title="Chi tiết sản phẩm" href="/productDetail/${product.productId}" class="fas fa-eye"></a>
                            </div>
                        </div>
                        <div class="product-content">
                            <div class="product-rating">
                                <i class="active icofont-star"></i>
                                <i class="active icofont-star"></i>
                                <i class="active icofont-star"></i>
                                <i class="active icofont-star"></i>
                                <i class="icofont-star"></i>
                            </div>
                            <h6 class="product-name">
                                <a href="/productDetail?id=${product.productId}">${product.productName}</a>
                            </h6>
                            <h6 class="product-price">
                                ${product.discount > 0 ? `<del>${formatCurrency(product.price)}</del>` : ''}
                                <span>${formatCurrency(discountedPrice)}</span>
                            </h6>
                            <a class="product-add" href="#">
                                <i class="fas fa-shopping-basket"></i> ${stockStatus}
                            </a>
                        </div>
                    </div>
                </div>
            `;
            $('#list').append(row);
        });
    }

    function renderPagination(categoryId, totalPages, currentPage) {
        $('.pagination').empty();
        if (totalPages <= 1) return;

        // Thêm nút Previous
        if (currentPage > 0) {
            $('.pagination').append(`
                <li class="page-item">
                    <a class="page-link" href="#" data-page="${currentPage-1}" data-category="${categoryId}">
                        &laquo;
                    </a>
                </li>
            `);
        }

        // Các nút trang
        for (let i = 0; i < totalPages; i++) {
            const activeClass = (i === currentPage) ? 'active' : '';
            $('.pagination').append(`
                <li class="page-item">
                    <a class="page-link ${activeClass}" href="#" data-page="${i}" data-category="${categoryId}">
                        ${i + 1}
                    </a>
                </li>
            `);
        }

        // Thêm nút Next
        if (currentPage < totalPages - 1) {
            $('.pagination').append(`
                <li class="page-item">
                    <a class="page-link" href="#" data-page="${currentPage+1}" data-category="${categoryId}">
                        &raquo;
                    </a>
                </li>
            `);
        }
    }

    // Sự kiện phân trang
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        const page = $(this).data('page');
        const categoryId = $(this).data('category');
        fetchProductsByCategory(categoryId, page, 12);
    });
});