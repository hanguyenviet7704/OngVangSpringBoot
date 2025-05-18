$(document).ready(function () {
    fetchCategories();
});
// Fetch all categories
function fetchCategories(page = 0, size = 5) {
    $.ajax({
        url: "http://localhost:8081/api/orders",
        type: "GET",
        data: { page: page, size: size },
        success: function (response) {
            renderOrders(response.content); // Gọi đúng tên hàm mới
            renderPagination(response.totalPages, response.pageNumber);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API danh mục:", err);
        }
    });
}
// Render categories into table body
function renderOrders(orders) {
    $('#list').empty();

    const formatCurrency = number => number.toLocaleString('vi-VN') + ' đ';
    const getStatusHTML = (status) => {
        switch (status) {
            case 0:
                return `<div style="cursor: pointer; color: #ff9900;">
                            <i class="fa fa-clock"> Chờ xác nhận</i>
                        </div>`;
            case 1:
                return `<div style="cursor: pointer; color: #007bff;">
                            <i class="fa fa-truck"> Đang giao hàng</i>
                        </div>`;
            case 2:
                return `<div style="cursor: pointer; color: #119744;">
                            <i class="fa fa-check-circle"> Đã thanh toán</i>
                        </div>`;
            case 3:
                return `<div style="cursor: pointer; color: red;">
                            <i class="fa fa-times-circle"> Đã hủy</i>
                        </div>`;
            default:
                return `<div style="cursor: pointer; color: gray;">
                            <i class="fa fa-question-circle"> Không rõ</i>
                        </div>`;
        }
    };

    orders.forEach(order => {
        const quantity = order.orderDetails.reduce((sum, item) => sum + item.quantity, 0);
        const row = `
            <tr role="row">
                <td class="sorting_1">${order.orderId}</td>
                <td>${order.address}</td>
                <td>${quantity}</td>
                <td>${order.orderDate}</td>
                <td>${order.phone}</td>
                <td>${formatCurrency(order.amount)}</td>
                <td>
                    <a href="/admin/order/detail/${order.orderId}">
                        <i class="fa fa-info-circle"> Chi tiết đơn hàng</i>
                    </a>
                </td>
                <td class="text-center">
                    ${getStatusHTML(order.status)}
                </td>
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
