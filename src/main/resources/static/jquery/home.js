$(document).ready(function () {
    fetchProducts();
});

function fetchProducts() {
    $.ajax({
        url: "http://localhost:8081/api/products",
        type: "GET",
        data: { page: 0, size: 100 },
        success: function (response) {
            console.log("Dữ liệu API:", response);
            renderProduct1(response);
            renderProduct2(response);
            renderProduct3(response);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API sản phẩm:", err);
        }
    });
}function renderProduct1(response) {
    $('#list1').empty();
    let i = -5; // Starting index for slick-slide
    response.content.forEach(product => {
        // Calculate discounted price
        const discountedPrice = product.price * (1 - product.discount / 100);
        // Determine if the product is new (within 7 days)
        const enteredDate = new Date(product.enteredDate);
        const currentDate = new Date();
        const diffTime = Math.abs(currentDate - enteredDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const isNew = diffDays <= 7;

        // Determine if product is favorited
        const wishClass = product.favorite ? 'active' : '';

        let row = `
        <li class="slick-slide slick-cloned" data-slick-index="${i++}" aria-hidden="true" tabindex="-1" style="width: 236px;">
            <div class="product-card">
                <div class="product-media">
                    <div class="product-label">
                        ${isNew ? '<label class="label-text new">mới về</label>' : ''}
                        ${product.discount > 0 ? `<label class="label-text sale">- ${product.discount}%</label>` : ''}
                    </div>
                    <button class="product-wish wish ${wishClass}" tabindex="-1">
                        <i class="fas fa-heart"></i>
                    </button>
                    <a class="product-image" href="/productDetail/${product.productId}" tabindex="-1">
                        <img src="/loadImage?imageName=${product.productImage}" alt="${product.productName}">
                    </a>
                    <div class="product-widget">
                        <a title="Chi tiết sản phẩm" href="/productDetail/${product.productId}" class="fas fa-eye" tabindex="-1"></a>
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
                        <a href="/productDetail?id=${product.productId}" tabindex="-1">${product.productName}</a>
                    </h6>
                    <h6 class="product-price">
                        ${product.discount > 0 ? `<del>${product.price.toLocaleString()} đ</del>` : ''}
                        <span>${discountedPrice.toLocaleString()} đ<small>/kg</small></span>
                    </h6>
                    <a class="product-add1" title="Thêm giỏ hàng" href="/addToCart?productId=${product.productId}" tabindex="-1">
                        <i class="fas fa-shopping-basket"></i><span>Thêm giỏ hàng</span>
                    </a>
                </div>
            </div>
        </li>
        `;
        $('#list1').append(row);
    });
}
function renderProduct2(response) {
    $('#list2').empty();
    let i = 50; // Starting index for slick-slide
    response.content.forEach(product => {
        // Calculate discounted price
        const discountedPrice = product.price * (1 - product.discount / 100);
        // Determine if the product is new (within 7 days)
        const enteredDate = new Date(product.enteredDate);
        const currentDate = new Date();
        const diffTime = Math.abs(currentDate - enteredDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const isNew = diffDays <= 7;

        // Determine if product is favorited
        const wishClass = product.favorite ? 'active' : '';

        let row = `
        <li class="slick-slide slick-cloned" data-slick-index="${i++}" aria-hidden="true" tabindex="-1" style="width: 236px;">
            <div class="product-card">
                <div class="product-media">
                    <div class="product-label">
                        ${isNew ? '<label class="label-text new">mới về</label>' : ''}
                        ${product.discount > 0 ? `<label class="label-text sale">- ${product.discount}%</label>` : ''}
                    </div>
                    <button class="product-wish wish ${wishClass}" tabindex="-1">
                        <i class="fas fa-heart"></i>
                    </button>
                    <a class="product-image" href="/productDetail/${product.productId}" tabindex="-1">
                        <img src="/loadImage?imageName=${product.productImage}" alt="${product.productName}">
                    </a>
                    <div class="product-widget">
                        <a title="Chi tiết sản phẩm" href="/productDetail?id=${product.productId}" class="fas fa-eye" tabindex="-1"></a>
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
                        <a href="/productDetail?id=${product.productId}" tabindex="-1">${product.productName}</a>
                    </h6>
                    <h6 class="product-price">
                        ${product.discount > 0 ? `<del>${product.price.toLocaleString()} đ</del>` : ''}
                        <span>${discountedPrice.toLocaleString()} đ<small>/kg</small></span>
                    </h6>
                    <a class="product-add1" title="Thêm giỏ hàng" href="/addToCart?productId=${product.productId}" tabindex="-1">
                        <i class="fas fa-shopping-basket"></i><span>Thêm giỏ hàng</span>
                    </a>
                </div>
            </div>
        </li>
        `;
        $('#list2').append(row);
    });
}function renderProduct3(response) {
    $('#list3').empty();
    let i = 50; // Starting index for slick-slide
    response.content.forEach(product => {
        // Calculate discounted price
        const discountedPrice = product.price * (1 - product.discount / 100);
        // Determine if the product is new (within 7 days)
        const enteredDate = new Date(product.enteredDate);
        const currentDate = new Date();
        const diffTime = Math.abs(currentDate - enteredDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const isNew = diffDays <= 7;

        // Determine if product is favorited
        const wishClass = product.favorite ? 'active' : '';
        let row = `
        <li class="slick-slide slick-cloned" data-slick-index="${i++}" aria-hidden="true" tabindex="-1" style="width: 236px;">
            <div class="product-card">
                <div class="product-media">
                    <div class="product-label">
                        ${isNew ? '<label class="label-text new">mới về</label>' : ''}
                        ${product.discount > 0 ? `<label class="label-text sale">- ${product.discount}%</label>` : ''}
                    </div>
                    <button class="product-wish wish ${wishClass}" tabindex="-1">
                        <i class="fas fa-heart"></i>
                    </button>
                    <a class="product-image" href="/productDetail/${product.productId}" tabindex="-1">
                        <img src="/loadImage?imageName=${product.productImage}" alt="${product.productName}">
                    </a>
                    <div class="product-widget">
                        <a title="Chi tiết sản phẩm" href="/productDetail?id=${product.productId}" class="fas fa-eye" tabindex="-1"></a>
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
                        <a href="/productDetail?id=${product.productId}" tabindex="-1">${product.productName}</a>
                    </h6>
                    <h6 class="product-price">
                        ${product.discount > 0 ? `<del>${product.price.toLocaleString()} đ</del>` : ''}
                        <span>${discountedPrice.toLocaleString()} đ<small>/kg</small></span>
                    </h6>
                    <a class="product-add1" title="Thêm giỏ hàng" href="/addToCart?productId=${product.productId}" tabindex="-1">
                        <i class="fas fa-shopping-basket"></i><span>Thêm giỏ hàng</span>
                    </a>
                </div>
            </div>
        </li>
        `;
        $('#list3').append(row);
    });
}