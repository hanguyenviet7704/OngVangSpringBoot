$(document).ready(function () {
    const pathSegments = window.location.pathname.split('/');
    const lastSegment = pathSegments.pop() || pathSegments.pop(); // handle possible trailing slash
        fetchProductsByProductID(lastSegment);
});
function fetchProductsByProductID(productID) {
    $.ajax({
        url: "http://localhost:8081/api/product/" + productID,
        type: "GET",
        success: function (response) {
            renderProduct(response);
            fetchProductByCategory(response.categoryId);
        },
        error: function (err) {
            console.error("Lỗi nè:", err);
        }
    });
}function  fetchProductByCategory(categoryId){
    $.ajax({
        url: "http://localhost:8081/api/find/productsByCategory/",
        type: "GET",
        data: {categoryID: categoryId},
        success: function (response) {
            console.log("Dữ liệu sản phẩm theo Category:", response);
            renderProductSameCategory(response.content); // <- sửa ở đây
        },
        error: function (err) {
            console.error("Lỗi khi gọi API sản phẩm theo category:", err);
        }
    });
}function renderProductSameCategory(products) {
    $('#list').empty();
    for (let i = 0; i < Math.min(5, products.length); i++) {
        const p = products[i];
        const discountedPrice = p.price * (1 - p.discount / 100);
        const row = `
        <li class="slick-slide slick-current slick-active" data-slick-index="${i}" aria-hidden="false" tabindex="0" style="width: 232px;">
            <div class="product-card">
                <div class="product-media">
                    <div class="product-label">
                        ${p.discount > 0 ? `<label class="label-text sale">- ${p.discount}%</label>` : ''}
                    </div>
                    <button class="product-wish" tabindex="0">
                        <i class="fas fa-heart"></i>
                    </button>
                    <a class="product-image" href="javascript:void(0);" tabindex="0">
                        <img src="/loadImage?imageName=${p.productImage}" alt="product">
                    </a>
                    <div class="product-widget">
                        <a title="Hãy đăng nhập" href="/login" class="fas fa-heart" tabindex="0"></a>
                        <a title="Chi tiết sản phẩm" href="/productDetail/${p.productId}" class="fas fa-eye" tabindex="0"></a>
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
                        <a href="/productDetail/${p.productId}" tabindex="0">${p.productName}</a>
                    </h6>
                    <h6 class="product-price">
                        ${p.discount > 0 ? `<del>${p.price.toLocaleString()} đ</del>` : ''}
                        <span>${discountedPrice.toLocaleString()} đ<small> /kg</small></span>
                    </h6>
                    <a class="product-add1" title="Thêm giỏ hàng" href="/addToCart?productId=${p.productId}" tabindex="0">
                        <i class="fas fa-shopping-basket"></i><span>Thêm giỏ hàng</span>
                    </a>
                </div>
            </div>
        </li>
        `;
        $('#list').append(row);
    }
}

function renderProduct(response) {
    $('#detail').empty();
    $('#detail').append(
        `<p>${response.description}</p>`
    )
    $('#product_detail').empty();
        const row = `
           	<div class="row">
				<div class="col-lg-6">
					<div class="details-gallery">
						<div class="details-label-group">
							<label class="details-label new">new</label>
							<label class="details-label off">- 2%</label>
						</div>
						<ul class="details-preview slick-initialized slick-slider">
							<div class="slick-list draggable"><div class="slick-track" style="opacity: 1; width: 566px;"><li class="slick-slide slick-current slick-active" data-slick-index="0" aria-hidden="false" tabindex="0" style="width: 566px; position: relative; left: 0px; top: 0px; z-index: 999; opacity: 1;"><img src="/loadImage?imageName=${response.productImage}" alt="product"></li></div></div>
						</ul>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="details-content">
						<h3 class="details-name">
							<a href="#">${response.productName} </a>
						</h3>
						<div class="details-meta">
							<p>Mã sản phẩm:<span>${response.productId} </span></p>
							<p>Thể loại:<a href="#">Hạt dinh dưỡng</a></p>
						</div>
						<div class="details-meta">
							<p>Số lượng hàng:<a href="#">${response.quantity}</a></p>
						</div>
						<div class="details-rating">
							<i class="active icofont-star"></i><i class="active icofont-star"></i><i class="active icofont-star"></i><i class="active icofont-star"></i>
							<i class="icofont-star"></i>
						</div>
						<h3 class="details-price">
							<del>${response.price}</del>
							<span>${response.price}<small>/kg</small></span>
						</h3>
						<p class="details-desc">
							${response.description}
						</p>
						<div class="details-list-group">
							<label class="details-list-title">Xem thêm:</label>
							<ul class="details-tag-list">
								<li>
									<a href="/productsByCategory/4">Rau củ</a>
								</li>
								<li>
									<a href="/productsByCategory/5">Trái cây</a>
								</li>
								<li>
									<a href="/productsByCategory/6">Củ quả</a>
								</li>
								<li>
									<a href="/productsByCategory/11">Hạt dinh dưỡng</a>
								</li>
								<li>
									<a href="/productsByCategory/12">Đặt sản vùng miền</a>
								</li>
							</ul>
						</div>
						<div class="details-list-group">
							<label class="details-list-title">Share:</label>
							<ul class="details-share-list">
								<li>
									<a href="javascript:void(0);" class="icofont-facebook" title="Facebook"></a>
								</li>
								<li>
									<a href="javascript:void(0);" class="icofont-twitter" title="Twitter"></a>
								</li>
								<li>
									<a href="javascript:void(0);" class="icofont-linkedin" title="Linkedin"></a>
								</li>
								<li>
									<a href="javascript:void(0);" class="icofont-instagram" title="Instagram"></a>
								</li>
							</ul>
						</div>
						<div class="details-add-group">
							<a class="product-add1" style="background-color: #119744" title="Thêm giỏ hàng" href="/addToCart?productId=65">
								<i style="color: white" class="fas fa-shopping-basket"></i>
								<span style="color: white">Thêm giỏ hàng</span>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
        `;
        $('#product_detail').append(row);
}