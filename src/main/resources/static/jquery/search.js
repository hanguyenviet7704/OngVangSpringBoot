$(document).ready(function() {
    let userId;
    // Kiểm tra đăng nhập
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

    // Khởi tạo
    checkLogin().then(() => {
        initSearchHandlers();
    });

    // Xử lý form tìm kiếm
    function initSearchHandlers() {
        $('.header-form').on('submit', function(e) {
            e.preventDefault();
            const keyword = $(this).find('input[name="keyword"]').val().trim();
            if (keyword) {
                $('#list').html('<div class="col-12 text-center"><div class="spinner-border text-success" role="status"><span class="visually-hidden">Loading...</span></div></div>');
                searchProducts(keyword);
            }
        });
    }

    // Hàm tìm kiếm sản phẩm
    function searchProducts(keyword) {
        $.ajax({
            url: "http://localhost:8081/api/find/products",
            type: "GET",
            data: {
                name: keyword,
                page: 0,
                size: 100
            },
            success: function(response) {
                if (response && response.content) {
                    renderProduct(response.content);
                    renderPagination(null, response.totalPages, response.pageNumber);
                } else {
                    showError('Không tìm thấy sản phẩm phù hợp.');
                }
            },
            error: function(err) {
                console.error("Lỗi khi tìm kiếm sản phẩm:", err);
                showError('Có lỗi xảy ra khi tìm kiếm. Vui lòng thử lại sau.');
            }
        });
    }

    // Hàm render sản phẩm
    function renderProduct(products) {
        $('#list').empty();
        if (!products || products.length === 0) {
            showError('Không tìm thấy sản phẩm phù hợp.');
            return;
        }

        // Hiển thị số lượng kết quả
        $('#list').append(`<div class="col-12 mb-4"><h5>Tìm thấy ${products.length} sản phẩm</h5></div>`);

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

    // Hàm render phân trang
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

    // Hàm hiển thị lỗi
    function showError(message) {
        $('#list').html(`
            <div class="col-12 text-center">
                <div class="alert alert-info" role="alert">
                    <i class="fas fa-info-circle"></i> ${message}
                </div>
            </div>
        `);
    }

    // Xử lý thêm vào giỏ hàng
    $(document).on('click', '.product-add', function(e) {
        e.preventDefault();
        const productId = $(this).closest('.product-card').data('product-id');

        if ($(this).find('span').text() === "Hết hàng") {
            alert("Sản phẩm đã hết hàng!");
            return;
        }

        if (!userId) {
            alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
            return;
        }

        $.ajax({
            url: `http://localhost:8081/api/cart/${userId}/items`,
            type: "POST",
            data: {
                productId: productId,
                quantity: 1
            },
            success: function(response) {
                // Cập nhật số lượng giỏ hàng
                location.reload();
                // Hiển thị thông báo thành công
            },
            error: function(err) {
                console.error("Lỗi thêm giỏ hàng:", err);

            }
        });
    });
}); 