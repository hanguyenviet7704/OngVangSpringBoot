$(document).ready(function () {
    // Khởi tạo biến userId
    let userId;

    // 1. Hàm lấy thông tin user hiện tại
    function getUserID() {
        return $.ajax({
            url: "http://localhost:8081/api/users/now",
            type: "GET"
        });
    }

    // 2. Hàm thêm sản phẩm vào giỏ hàng
    function addToCart(userId, productId, quantity = 1) {
        return $.ajax({
            url: `http://localhost:8081/api/cart/${userId}/items`,
            type: "POST",
            data: {
                productId: productId,
                quantity: quantity
            }
        });
    }

    // 3. Hàm xử lý sự kiện click thêm vào giỏ hàng
    function handleAddToCart() {
        $(document).on('click', '.product-add', function (e) {
            e.preventDefault();
            const productId = $(this).data('product-id');
            if (!userId) {
                alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
                return;
            }
            addToCart(userId, productId)
                .then(() => {

                    location.reload();
                })
                .catch((err) => {
                    console.error("Lỗi khi thêm vào giỏ hàng:", err);
                    alert("❌ Thêm vào giỏ hàng thất bại!");
                });
        });
    }

    // 4. Hàm hiển thị sản phẩm
    function renderProducts(products, containerId) {
        const $container = $(containerId);
        $container.empty();

        products.forEach((product, index) => {
            const discountedPrice = product.price * (1 - product.discount / 100);

            const productHtml = `
            <li class="slick-slide" data-slick-index="${index}" aria-hidden="false" tabindex="-1" style="width: 236px;">
                <div class="product-card">
                    <div class="product-media">
                        <div class="product-label">
                            ${product.discount > 0 ? `<label class="label-text sale">-${product.discount}%</label>` : ''}
                        </div>
               
                        <a class="product-image" href="/productDetail/${product.productId}" tabindex="-1">
                            <img src="/loadImage?imageName=${product.productImage}" alt="${product.productName}">
                        </a>
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
                            <a href="/productDetail/${product.productId}">${product.productName}</a>
                        </h6>
                        <h6 class="product-price">
                            ${product.discount > 0 ? `<del>${product.price.toLocaleString()}₫</del>` : ''}
                            <span>${discountedPrice.toLocaleString()}₫</span>
                        </h6>
                        <button class="product-add" data-product-id="${product.productId}" tabindex="-1">
                            <i class="fas fa-shopping-basket"></i>
                            <span>Thêm giỏ hàng</span>
                        </button>
                    </div>
                </div>
            </li>
            `;

            $container.append(productHtml);
        });
    }

    // 5. Hàm lấy danh sách sản phẩm từ API
    function fetchProducts() {
        $.ajax({
            url: "http://localhost:8081/api/products",
            type: "GET",
            data: { page: 0, size: 100 },
            success: function (response) {
                renderProducts(response.content, '#list1');
                renderProducts(response.content, '#list2');
                renderProducts(response.content, '#list3');
            },
            error: function (err) {
                console.error("Lỗi khi gọi API sản phẩm:", err);
            }
        });
    }

    // Khởi chạy chương trình
    getUserID()
        .then(function (response) {
            userId = response;
            handleAddToCart();
            fetchProducts();
        })
        .catch(function (err) {
            console.error("Không lấy được user ID:", err);
            fetchProducts();
        });
});