$(document).ready(function () {
    fetchCategories();
});
// Fetch all categories
function fetchCategories(page = 0, size = 10) {
    $.ajax({
        url: "http://localhost:8081/api/users",
        type: "GET",
        data: { page: page, size: size },
        success: function (response) {
            renderUsers(response.content); // Gọi đúng tên hàm mới
            renderPagination(response.totalPages, response.pageNumber);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API danh mục:", err);
        }
    });
}
// Render categories into table body
function renderUsers(users) {
    $('#list').empty();

    users.forEach(user => {
        const statusText = user.status ? 'Đang hoạt động' : 'Đã bị khóa';
        const statusClass = user.status ? 'text-success' : 'text-danger';
        const lockForm = `
            <form action="/admin/users/${user.userId}/lock" method="post">
                <button type="submit" class="btn btn-link btn-danger" title="Khóa tài khoản">
                    <i class="fa fa-lock"></i>
                </button>
            </form>`;
        const unlockForm = `
            <form action="/admin/users/${user.userId}/unlock" method="post">
                <button type="submit" class="btn btn-link btn-success" title="Mở khóa tài khoản">
                    <i class="fa fa-unlock"></i>
                </button>
            </form>`;

        const row = `
            <tr>
                <td>${user.userId}</td>
                <td>
                    <img src="/loadImage?imageName=${user.avatar}" alt="..." class="avatar-img rounded-circle" style="width: 60px">
                </td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.registerDate}</td>
                <td class="${statusClass}">${statusText}</td>
               
            </tr>
        `;
        $('#list').append(row);
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
