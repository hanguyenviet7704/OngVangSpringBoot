$(document).ready(function () {
    const pathSegments = window.location.pathname.split('/');
    const lastSegment = pathSegments.pop() || pathSegments.pop(); // handle possible trailing slash
    if (lastSegment === "products") {
        fetchProducts();
    } else {
        fetchProductsByCategory(lastSegment);
    }
});

// Fetch all products
function fetchProducts(page = 0, size = 12) {
    $.ajax({
        url: "http://localhost:8081/api/products",
        type: "GET",
        data: {page: page, size: size},
        success: function (response) {
            console.log("Dữ liệu API:", response);
            renderProduct(response.content);
            renderPagination(null, response.totalPages, response.pageNumber);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API sản phẩm:", err);
        }
    });
}

// Fetch products by category
function fetchProductsByCategory(categoryId, page = 0, size = 12) {
    $.ajax({
        url: "http://localhost:8081/api/find/productsByCategory/",
        type: "GET",
        data: {categoryID: categoryId, page: page, size: size},
        success: function (response) {
            console.log("Dữ liệu sản phẩm theo Category:", response);
            renderProduct(response.content);
            renderPagination(categoryId, response.totalPages, response.pageNumber);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API sản phẩm theo category:", err);
        }
    });
}

// Render sản phẩm
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
                <div class="product-card">
                    <div class="product-media">
                        <div class="product-label">
                            <label class="label-text sale">- ${product.discount}%</label>
                        </div>
                        <button class="product-wish">
                            <i class="fas fa-heart"></i>
                        </button>
                        <a class="product-image" href="/productDetail?id=${product.productId}">
                            <img src="/loadImage?imageName=${product.productImage}" alt="product">
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
                            <del>${formatCurrency(product.price)}</del>
                            <span>${formatCurrency(discountedPrice)}</span>
                        </h6>
                        <a class="product-add2">
                            <i class="fas fa-shopping-basket"></i> ${stockStatus}
                        </a>
                    </div>
                </div>
            </div>
        `;
        $('#list').append(row);
    });
}

// Render phân trang
function renderPagination(categoryId, totalPages, currentPage) {
    $('.pagination').empty();
    if (totalPages <= 1) return;
    for (let i = 0; i < totalPages; i++) {
        const activeClass = (i === currentPage) ? 'active' : '';
        const pageItem = `
            <li class="page-item ">
                <a class="page-link ${activeClass}" href="javascript:void(0);" data-page="${i}" data-category="${categoryId}">
                    ${i + 1}
                </a>
            </li>
        `;
        $('.pagination').append(pageItem);
    }
}
$(document).on('click', '.page-link', function (e) {
    e.preventDefault();
    const page = $(this).data('page');
    const categoryId = $(this).data('category');
    if (categoryId == null || categoryId === "null" || categoryId === "") {
        fetchProducts(page, 12);
    } else {
        fetchProductsByCategory(categoryId, page, 12);
    }
});

