$(document).ready(function() {
    // Kiểm tra xem đang ở trang nào
    const currentPath = window.location.pathname;
    
    if (currentPath.includes('/admin/editProduct/')) {
        // Xử lý cho trang edit product
        handleEditProduct();
    } else if (currentPath === '/admin/products') {
        // Xử lý cho trang danh sách product
        handleProductList();
    }
});

// Xử lý cho trang danh sách product
function handleProductList() {
    fetchProducts();
    loadCategories('#categoryId');

    // Handle add product form submission
    $('#addProductForm').on('submit', function(e) {
        e.preventDefault();

        const formData = new FormData();
        
        // Append DTO fields manually
        const productDTO = {
            productName: $('#productName').val(),
            quantity: $('#quantity').val(),
            price: $('#price').val(),
            discount: $('#discount').val(),
            description: $('#description').val(),
            categoryId: $('#categoryId').val(),
            enteredDate:  new Date().toISOString().split('T')[0],
            // status: $('#status').is(':checked')
        };

        // Basic Validation (more comprehensive validation should be done)
        if (!productDTO.productName) { alert('Vui lòng nhập tên sản phẩm'); return; }
        if (!productDTO.categoryId) { alert('Vui lòng chọn thể loại'); return; }
        if (!productDTO.price || productDTO.price <= 0) { alert('Vui lòng nhập giá sản phẩm hợp lệ'); return; }
        if (!productDTO.quantity || productDTO.quantity < 0) { alert('Vui lòng nhập số lượng hợp lệ'); return; }

        formData.append('productDTO', new Blob([JSON.stringify(productDTO)], { type: 'application/json' }));

        // Append file
        const fileInput = $('#productImage')[0];
        if (fileInput.files && fileInput.files[0]) {
            formData.append('file', fileInput.files[0]);
        } else {
            alert('Vui lòng chọn hình ảnh sản phẩm'); // Image is required for new product
            return;
        }

        $.ajax({
            url: '/api/products',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                $('#addRowModal').modal('hide');
                fetchProducts();
                $('#addProductForm')[0].reset();
                alert('Thêm sản phẩm thành công');
            },
            error: function(xhr) {
                alert('Lỗi khi thêm sản phẩm: ' + (xhr.responseJSON?.message || xhr.responseText || 'Vui lòng thử lại sau'));
            }
        });
    });

    // Handle delete product
    $('#yesOption').on('click', function() {
        const productId = $(this).data('id');
        
        $.ajax({
            url: '/api/products/' + productId,
            type: 'DELETE',
            success: function(response) {
                $('#configmationId').modal('hide');
                fetchProducts();
                alert('Xóa sản phẩm thành công');
            },
            error: function(xhr) {
                alert('Sản phẩm trong giỏ hàng hoặc đang trên đường giao.' );
            }
        });
    });
}

// Load categories for select box
function loadCategories(selectId = '#categoryId', callback) {
    $.ajax({
        url: '/api/categoris',
        type: 'GET',
        success: function(response) {
            const select = $(selectId);
            select.empty();
            select.append('<option value="">Chọn thể loại</option>');
            if (response && response.content) {
                response.content.forEach(function(category) {
                    select.append(`<option value="${category.categoryId}">${category.categoryName}</option>`);
                });
            }
            // Call the callback function after categories are loaded
            if (typeof callback === 'function') {
                callback();
            }
        },
        error: function(xhr) {
            console.error('Lỗi khi tải danh mục:', xhr);
        }
    });
}

// Xử lý cho trang edit product
function handleEditProduct() {
    // Lấy productId từ URL path
    const pathParts = window.location.pathname.split('/');
    const productId = pathParts[pathParts.length - 1];

    // Load categories
    loadCategories('#editCategoryId', function() {
        // Lấy thông tin product
        $.ajax({
            url: '/api/product/' + productId,
            type: 'GET',
            success: function(response) {
                if (response) {
                    $('#editProductId').val(response.productId);
                    $('#editProductName').val(response.productName);
                    $('#editQuantity').val(response.quantity);
                    $('#editPrice').val(response.price);
                    $('#editDiscount').val(response.discount);
                    $('#editDescription').val(response.description);
                    $('#editCategoryId').val(response.categoryId);
                    if (response.status) {
                        $('#editStatus').prop('checked', true);
                    }
                    if (response.productImage) {
                        $('#currentProductImage').attr('src', '/loadImage?imageName=' + response.productImage);
                    } else {
                        $('#currentProductImage').attr('src', '');
                    }
                }
            },
            error: function(xhr, status, error) {
                alert('Không thể lấy thông tin sản phẩm. Vui lòng thử lại sau.');
                window.location.replace('/admin/products');
            }
        });
    });

    // Xử lý form submit
    $('#editProductForm').submit(function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const productId = $('#editProductId').val();
        const productName = $('#editProductName').val();
        const categoryId = $('#editCategoryId').val();
        const price = $('#editPrice').val();
        const quantity = $('#editQuantity').val();

        if (!productName) {
            alert('Vui lòng nhập tên sản phẩm');
            return;
        }
        if (!categoryId) {
            alert('Vui lòng chọn thể loại');
            return;
        }
        if (!price || price <= 0) {
            alert('Vui lòng nhập giá sản phẩm hợp lệ');
            return;
        }
        if (!quantity || quantity < 0) {
            alert('Vui lòng nhập số lượng hợp lệ');
            return;
        }

        $.ajax({
            url: '/api/products/' + productId,
            type: 'PUT',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert('Cập nhật sản phẩm thành công');
                window.location.replace('/admin/products');
            },
            error: function(xhr, status, error) {
                alert('Không thể cập nhật sản phẩm: ' + (xhr.responseJSON?.message || xhr.responseText || 'Vui lòng thử lại sau'));
            }
        });
    });
}

// Fetch all products
function fetchProducts(page = 0, size = 5) {
    $.ajax({
        url: "/api/products",
        type: "GET",
        data: { page: page, size: size },
        success: function(response) {
            if (response && response.content) {
                renderProducts(response.content);
                renderPagination(response.totalPages, response.pageNumber);
            }
        },
        error: function(err) {
            console.error("Lỗi khi gọi API sản phẩm:", err);
        }
    });
}

// Render products into table body
function renderProducts(products) {
    const tbody = $('#product-table-body');
    tbody.empty();
    if (products && products.length > 0) {
        products.forEach((product, index) => {
            const row = `
                <tr role="row" class="${index % 2 === 0 ? 'odd' : 'even'}">
                    <td class="sorting_1">${product.productId}</td>
                    <td>
                        <img src="/loadImage?imageName=${product.productImage}" width="80px" alt="product">
                    </td>
                    <td>${product.productName}</td>
                    <td>${product.categoryName || 'N/A'}</td>
                    <td>${product.price ? product.price.toLocaleString() + ' đ' : 'N/A'}</td>
                    <td>${product.discount || 0}%</td>
                    <td>${product.quantity || 0}</td>
                    <td>${product.enteredDate || 'N/A'}</td>
                    <td>
                        <div class="form-button-action">
                            <button type="button" class="btn btn-link btn-primary btn-lg edit-product-btn" data-id="${product.productId}" title="Chỉnh sửa" data-toggle="modal" data-target="#editRowModal">
                                <i class="fa fa-edit"></i>
                            </button>
                            <button 
                                data-id="${product.productId}" 
                                data-name="${product.productName}" 
                                onclick="showConfigModalDialog(this.getAttribute('data-id'), this.getAttribute('data-name'))" 
                                type="button" 
                                class="btn btn-link btn-danger" 
                                title="Xóa">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    } else {
        tbody.append('<tr><td colspan="9" class="text-center">Không có sản phẩm nào</td></tr>');
    }
}

// Render custom pagination
function renderPagination(totalPages, currentPage) {
    const pagination = $('#custom-pagination');
    pagination.empty();
    if (totalPages < 1) return;
    // Previous button
    const prevDisabled = currentPage === 0 ? 'disabled' : '';
    pagination.append(`
        <li class="page-item ${prevDisabled}">
            <a class="page-link" href="javascript:void(0);" data-page="${currentPage - 1}">Previous</a>
        </li>
    `);
    // Page numbers
    for (let i = 0; i < totalPages; i++) {
        const activeClass = i === currentPage ? 'active' : '';
        pagination.append(`
            <li class="page-item ${activeClass}">
                <a class="page-link" href="javascript:void(0);" data-page="${i}">${i + 1}</a>
            </li>
        `);
    }
    // Next button
    const nextDisabled = currentPage === totalPages - 1 ? 'disabled' : '';
    pagination.append(`
        <li class="page-item ${nextDisabled}">
            <a class="page-link" href="javascript:void(0);" data-page="${currentPage + 1}">Next</a>
        </li>
    `);
}

// Handle pagination click
$(document).on('click', '#custom-pagination .page-link', function(e) {
    e.preventDefault();
    const page = $(this).data('page');
    if (page >= 0) {
        fetchProducts(page, 5);
    }
});

// Handle delete product modal
function showConfigModalDialog(id, name) {
    $('#yesOption').data('id', id);
    $('#configmationId').modal('show');
}

// Set current date for enteredDate input in add modal when shown
$('#addRowModal').on('show.bs.modal', function () {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0!
    const dd = String(today.getDate()).padStart(2, '0');
    const formattedDate = yyyy + '-' + mm + '-' + dd;
    $('#enteredDate').val(formattedDate);
});

// Append Edit Product Modal to body
const editModalHtml = `
    <div class="modal fade" id="editRowModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header no-bd">
                    <h5 class="modal-title">
                        <span class="fw-mediumbold" style="text-decoration: underline; color: black;">
                        Chỉnh sửa sản phẩm</span>
                    </h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="editProductForm" enctype="multipart/form-data">
                        <input type="hidden" id="editProductId" name="productId">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group form-group-default">
                                    <label>Tên sản phẩm</label>
                                    <input id="editProductName" type="text" class="form-control" placeholder="Tên sản phẩm..." name="productName" required>
                                </div>
                            </div>
                            <div class="col-md-6 pr-0">
                                <div class="form-group form-group-default">
                                    <label>Thể loại</label>
                                    <select class="form-control" name="categoryId" id="editCategoryId" required>
                                        <option value="">Chọn thể loại</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6 pr-0">
                                <div class="form-group form-group-default">
                                    <label>Đơn giá</label>
                                    <input id="editPrice" min="0" type="number" class="form-control" placeholder="Đơn giá" name="price" required>
                                </div>
                            </div>
                            <div class="col-md-6 pr-0">
                                <div class="form-group form-group-default">
                                    <label>Số lượng</label>
                                    <input id="editQuantity" min="0" type="number" class="form-control" placeholder="Số lượng" name="quantity" required>
                                </div>
                            </div>
                            <div class="col-md-6 pr-0">
                                <div class="form-group form-group-default">
                                    <label>Giảm giá (%)</label>
                                    <input id="editDiscount" min="0" max="100" type="number" class="form-control" placeholder="Giảm giá" name="discount" value="0">
                                </div>
                            </div>
                             <div class="col-sm-12">
                                <div class="form-group form-group-default">
                                    <label>Hình ảnh hiện tại</label>
                                    <img id="currentProductImage" src="" alt="Hình ảnh sản phẩm" style="max-width: 100px; margin-top: 10px;">
                                </div>
                            </div>
                            <div class="col-sm-12">
                                <div class="form-group form-group-default">
                                    <label>Chọn hình ảnh mới (nếu muốn thay đổi)</label>
                                    <input type="file" id="editProductImage" name="file" class="form-control">
                                </div>
                            </div>
                            <div class="col-sm-12">
                                <div class="form-group form-group-default">
                                    <label>Mô tả sản phẩm</label>
                                    <textarea id="editDescription" class="form-control" placeholder="Mô tả sản phẩm" name="description"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer no-bd">
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Hủy</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
`;

$('body').append(editModalHtml);

// Handle click on edit button
$(document).on('click', '.edit-product-btn', function() {
    const productId = $(this).data('id');

    // Fetch product data first
    $.ajax({
        url: '/api/product/' + productId,
        type: 'GET',
        success: function(response) {
            if (response) {
                // Load categories, then populate form including category
                loadCategories('#editCategoryId', function() {
                    $('#editProductId').val(response.productId);
                    $('#editProductName').val(response.productName);
                    $('#editQuantity').val(response.quantity);
                    $('#editPrice').val(response.price);
                    $('#editDiscount').val(response.discount);
                    $('#editDescription').val(response.description);
                    $('#editCategoryId').val(response.categoryId); // Set selected category NOW
                    if (response.status) {
                        $('#editStatus').prop('checked', true);
                    }
                    if (response.productImage) {
                         $('#currentProductImage').attr('src', '/loadImage?imageName=' + response.productImage);
                    } else {
                         $('#currentProductImage').attr('src', ''); // Clear image if none
                    }
                    // Show the modal (handled by data-toggle)
                });
            }
        },
        error: function(xhr) {
            alert('Không thể lấy thông tin sản phẩm: ' + (xhr.responseJSON?.message || xhr.responseText || 'Vui lòng thử lại sau'));
        }
    });
});

// Handle edit product form submission
$('#editProductForm').on('submit', function(e) {
    e.preventDefault();

    const productId = $('#editProductId').val();
    const formData = new FormData();

    // Append DTO fields manually
    const productDTO = {
        productId: productId,
        productName: $('#editProductName').val(),
        quantity: $('#editQuantity').val(),
        price: $('#editPrice').val(),
        discount: $('#editDiscount').val(),
        description: $('#editDescription').val(),
        categoryId: $('#editCategoryId').val()
        // status: $('#editStatus').is(':checked')
    };

    formData.append('productDTO', new Blob([JSON.stringify(productDTO)], { type: 'application/json' }));

    // Append file if selected
    const fileInput = $('#editProductImage')[0];
    if (fileInput.files && fileInput.files[0]) {
        formData.append('file', fileInput.files[0]);
    }

    $.ajax({
        url: '/api/products/' + productId,
        type: 'PUT',
        data: formData,
        processData: false, // Prevent jQuery from automatically processing the data
        contentType: false, // Prevent jQuery from automatically setting Content-Type
        success: function(response) {
            $('#editRowModal').modal('hide');
            fetchProducts(); // Refresh the product list
            alert('Cập nhật sản phẩm thành công');
        },
        error: function(xhr) {
            alert('Lỗi khi cập nhật sản phẩm: ' + (xhr.responseJSON?.message || xhr.responseText || 'Vui lòng thử lại sau'));
        }
    });
});
