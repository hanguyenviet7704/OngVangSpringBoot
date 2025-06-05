$(document).ready(function() {
    // Kiểm tra xem đang ở trang nào
    const currentPath = window.location.pathname;
    
    if (currentPath.includes('/admin/editCategory/')) {
        // Xử lý cho trang edit category
        handleEditCategory();
    } else if (currentPath === '/admin/categories') {
        // Xử lý cho trang danh sách category
        handleCategoryList();
    }
});

// Xử lý cho trang danh sách category
function handleCategoryList() {
    fetchCategories();

    // Handle add category form submission
    $('#addCategoryForm').on('submit', function(e) {
        e.preventDefault();
        const categoryName = $('#categoryName').val();
        
        $.ajax({
            url: '/api/categoris',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ categoryName: categoryName }),
            success: function(response) {
                $('#addRowModal').modal('hide');
                fetchCategories();
                $('#categoryName').val('');
            },
            error: function(xhr) {
                alert('Lỗi khi thêm thể loại: ' + xhr.responseText);
            }
        });
    });

    // Handle delete category
    $('#yesOption').on('click', function() {
        const categoryId = $(this).data('id');
        
        $.ajax({
            url: '/api/categoris/' + categoryId,
            type: 'DELETE',
            success: function(response) {
                $('#configmationId').modal('hide');
                fetchCategories();
            },
            error: function(xhr) {
                alert('Không thể xóa thể loại vì thể loại chứa sản phẩm trong hệ thống .'  );
            }
        });
    });
}

// Xử lý cho trang edit category
function handleEditCategory() {
    // Lấy categoryId từ URL path
    const pathParts = window.location.pathname.split('/');
    const categoryId = pathParts[pathParts.length - 1];

    // Lấy thông tin category
    $.ajax({
        url: '/api/categoris/' + categoryId,
        type: 'GET',
        success: function(response) {
            $('#categoryId').val(response.categoryId);
            $('#categoryName').val(response.categoryName);
        },
        error: function(xhr, status, error) {
            alert('Không thể lấy thông tin thể loại. Vui lòng thử lại sau.');
            window.location.replace('/admin/categories');
        }
    });

    // Xử lý form submit
    $('#editCategoryForm').submit(function(e) {
        e.preventDefault();
        
        const categoryName = $('#categoryName').val();
        if (!categoryName) {
            alert('Vui lòng nhập tên thể loại');
            return;
        }

        const formData = {
            categoryId: categoryId,
            categoryName: categoryName
        };

        $.ajax({
            url: '/api/categoris/' + categoryId,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                alert('Cập nhật thể loại thành công');
                window.location.replace('/admin/categories');
            },
            error: function(xhr, status, error) {
                alert('Không thể cập nhật thể loại. Vui lòng thử lại sau.');
            }
        });
    });
}

// Fetch all categories
function fetchCategories(page = 0, size = 5) {
    $.ajax({
        url: "/api/categoris",
        type: "GET",
        data: { page: page, size: size },
        success: function(response) {
            renderCategories(response.content);
            renderPagination(response.totalPages, response.pageNumber);
        },
        error: function(err) {
            console.error("Lỗi khi gọi API danh mục:", err);
        }
    });
}

// Render categories into table body
function renderCategories(categories) {
    const tbody = $('#category-table-body');
    tbody.empty();
    categories.forEach((category, index) => {
        const row = `
            <tr role="row" class="${index % 2 === 0 ? 'odd' : 'even'}">
                <td class="sorting_1">${category.categoryId}</td>
                <td>${category.categoryName}</td>
                <td>
                    <div class="form-button-action">
                        <a href="/admin/editCategory/${category.categoryId}" type="button" class="btn btn-link btn-primary btn-lg" title="Chỉnh sửa">
                            <i class="fa fa-edit"></i>
                        </a>
                        <button 
                            data-id="${category.categoryId}" 
                            data-name="${category.categoryName}" 
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
        fetchCategories(page, 5);
    }
});

// Handle delete category modal
function showConfigModalDialog(id, name) {
    $('#yesOption').data('id', id);
    $('#configmationId').modal('show');
}
