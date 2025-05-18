$(document).ready(function () {
    fetchCategories();
});
// Fetch all categories
function fetchCategories(page = 0, size = 5) {
    $.ajax({
        url: "http://localhost:8081/api/categoris",
        type: "GET",
        data: { page: page, size: size },
        success: function (response) {
            renderCategories(response.content);
            renderPagination(response.totalPages, response.pageNumber);
        },
        error: function (err) {
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
$(document).on('click', '#custom-pagination .page-link', function (e) {
    e.preventDefault();
    const page = $(this).data('page');
    if (page >= 0) {
        fetchCategories(page, 5);
    }
});
