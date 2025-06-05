$(document).ready(function() {
    // Load danh sách đơn hàng khi trang được tải
    loadOrders();
});

// Hàm load danh sách đơn hàng
function loadOrders(page = 0, size = 10) {
    $.ajax({
        url: '/api/orders',
        type: 'GET',
        data: {
            page: page,
            size: size
        },
        success: function(response) {
            renderOrders(response.content);
            renderPagination(response.totalPages, response.pageNumber);
            updateTableInfo(response);
        },
        error: function(xhr) {
            alert('Có lỗi xảy ra khi tải danh sách đơn hàng!');
        }
    });
}

// Hàm render danh sách đơn hàng
function renderOrders(orders) {
    const tbody = $('#list');
    tbody.empty();

    orders.forEach(order => {
        const row = `
            <tr role="row">
                <td class="sorting_1">${order.orderId}</td>
                <td>${order.userId || 'N/A'}</td>
                <td>${order.phone}</td>
                <td>${formatDate(order.orderDate)}</td>
                <td>${order.address}</td>
                <td>${formatCurrency(order.amount)}</td>
                <td>${renderStatusBadge(order.status)}</td>
               
                <td>
                    ${renderActionButtons(order)}
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

// Hàm render badge trạng thái
function renderStatusBadge(status) {
    const statusMap = {
        0: { text: 'Chờ xác nhận', class: 'badge-warning' },
        1: { text: 'Đã xác nhận', class: 'badge-info' },
        2: { text: 'Đã giao', class: 'badge-success' },
        3: { text: 'Đã hủy', class: 'badge-danger' }
    };

    const statusInfo = statusMap[status] || { text: 'Không xác định', class: 'badge-secondary' };
    return `<span class="badge ${statusInfo.class}">${statusInfo.text}</span>`;
}

// Hàm render các nút hành động dựa vào trạng thái đơn hàng
function renderActionButtons(order) {
    let buttons = '';
    
    switch(order.status) {
        case 0: // PENDING
            buttons += `
                <button class="btn btn-success btn-sm" onclick="updateOrderStatus(${order.orderId}, 1)">
                    <i class="fa fa-check"></i> Xác nhận
                </button>
                <button class="btn btn-danger btn-sm" onclick="updateOrderStatus(${order.orderId}, 3)">
                    <i class="fa fa-times"></i> Hủy
                </button>
            `;
            break;
        case 1: // CONFIRMED
            buttons += `
                <button class="btn btn-info btn-sm" onclick="updateOrderStatus(${order.orderId}, 2)">
                    <i class="fa fa-truck"></i> Đã giao
                </button>
            `;
            break;
        case 2: // DELIVERED
            buttons += `
                <span class="badge badge-success">
                    <i class="fa fa-check-circle"></i> Đã giao hàng
                </span>
            `;
            break;
        case 3: // CANCELLED
            buttons += `
                <span class="badge badge-danger">
                    <i class="fa fa-times-circle"></i> Đã hủy
                </span>
            `;
            break;
    }
    
    return buttons;
}

// Hàm cập nhật trạng thái đơn hàng
function updateOrderStatus(orderId, newStatus) {
    let url = '';
    let method = '';
    let successMessage = '';

    switch(newStatus) {
        case 1: // CONFIRMED
            url = `/api/order/confirm/${orderId}`;
            method = 'PUT';
            successMessage = 'Xác nhận đơn hàng thành công!';
            break;
        case 2: // DELIVERED
            url = `/api/order/delivered/${orderId}`;
            method = 'PUT';
            successMessage = 'Cập nhật trạng thái đơn hàng thành công!';
            break;
        case 3: // CANCELLED
            url = `/api/order/cancel/${orderId}`;
            method = 'PUT';
            successMessage = 'Hủy đơn hàng thành công!';
            break;
    }

    if (url && method) {
        $.ajax({
            url: url,
            type: method,
            success: function(response) {
                loadOrders();
                alert(successMessage);
            },
            error: function(xhr) {
                alert('Có lỗi xảy ra khi cập nhật trạng thái đơn hàng!');
            }
        });
    }
}

// Hàm render phân trang
function renderPagination(totalPages, currentPage) {
    const pagination = $('#custom-pagination');
    pagination.empty();

    // Nút Previous
    const prevDisabled = currentPage === 0 ? 'disabled' : '';
    pagination.append(`
        <li class="paginate_button page-item previous ${prevDisabled}" id="add-row_previous">
            <a href="#" aria-controls="add-row" data-dt-idx="0" tabindex="0" class="page-link">Previous</a>
        </li>
    `);

    // Các số trang
    for (let i = 0; i < totalPages; i++) {
        const activeClass = i === currentPage ? 'active' : '';
        pagination.append(`
            <li class="paginate_button page-item ${activeClass}">
                <a href="#" aria-controls="add-row" data-dt-idx="${i + 1}" tabindex="0" class="page-link">${i + 1}</a>
            </li>
        `);
    }

    // Nút Next
    const nextDisabled = currentPage === totalPages - 1 ? 'disabled' : '';
    pagination.append(`
        <li class="paginate_button page-item next ${nextDisabled}" id="add-row_next">
            <a href="#" aria-controls="add-row" data-dt-idx="${totalPages}" tabindex="0" class="page-link">Next</a>
        </li>
    `);
}

// Hàm cập nhật thông tin bảng
function updateTableInfo(response) {
    const start = response.pageNumber * response.pageSize + 1;
    const end = Math.min(start + response.pageSize - 1, response.totalElements);
    $('#add-row_info').text(`Showing ${start} to ${end} of ${response.totalElements} entries`);
}

// Hàm format ngày tháng
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
}

// Hàm format tiền tệ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND' 
    }).format(amount);
}

// Hàm xem chi tiết đơn hàng
function viewOrderDetail(orderId) {
    window.location.href = '/admin/order/' + orderId;
}

// Xử lý sự kiện phân trang
$(document).on('click', '#custom-pagination .page-link', function(e) {
    e.preventDefault();
    const page = $(this).data('dt-idx') - 1;
    if (page >= 0) {
        loadOrders(page);
    }
});

// Xử lý sự kiện thay đổi số lượng hiển thị
$('#add-row_length select').on('change', function() {
    const size = $(this).val();
    loadOrders(0, size);
});
