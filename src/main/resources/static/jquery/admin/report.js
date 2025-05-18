$(document).ready(function () {
    const pathSegments = window.location.pathname.split('/');
    const lastSegment = pathSegments.pop() || pathSegments.pop(); // handle possible trailing slash
    fetchCategories(lastSegment);
});
// Fetch all categories
function fetchCategories(lastSegment) {
    let url = "";
    switch (lastSegment) {
        case "reportProducts":
            url = "http://localhost:8081/api/reports/products";
            break;
        case "reportCategory":
            url = "http://localhost:8081/api/reports/category";
            break;
        case "reportYear":
            url = "http://localhost:8081/api/reports/years";
            break;
        case "reportMonths":
            url = "http://localhost:8081/api/reports/months";
            break;
        case "reportQuater":
            url = "http://localhost:8081/api/reports/quater";
            break;
        case "reportCustomer":
            url = "http://localhost:8081/api/reports/customer";
            break;
        default:
            console.error("Trường hợp không hợp lệ");
            return;
    }

    $.ajax({
        url: url,
        type: "GET",
        success: function (response) {
            renderReport(response);
            console.log(response);
        },
        error: function (err) {
            console.error("Lỗi khi gọi API danh mục:", err);
        }
    });
}

function renderReport(statistics) {
    const tbody = $('#list');
    tbody.empty();

    statistics.forEach((row, index) => {
        const [
            group,
            orderCount,
            totalRevenue,
            avgOrderValue,
            minOrderValue,
            maxOrderValue
        ] = row;

        const html = `
            <tr>
                <td>${index + 1}</td>
                <td>${group}</td>
                <td>${orderCount}</td>
                <td>${formatCurrency(totalRevenue)}</td>
                <td>${formatCurrency(avgOrderValue)}</td>
                <td>${formatCurrency(minOrderValue)}</td>
                <td>${formatCurrency(maxOrderValue)}</td>
            </tr>
        `;

        tbody.append(html);
    });
}

function formatCurrency(value) {
    return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
}
