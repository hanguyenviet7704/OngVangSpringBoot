# OngVang - Hệ thống Quản lý Cửa hàng Thực phẩm

## Giới thiệu
OngVang là một hệ thống quản lý cửa hàng thực phẩm trực tuyến, được phát triển bằng Spring Boot và các công nghệ web hiện đại. Hệ thống cung cấp các chức năng quản lý sản phẩm, đơn hàng, giỏ hàng và tích hợp chatbot AI để hỗ trợ khách hàng.

## Tính năng chính

### 1. Quản lý Sản phẩm
- Thêm, sửa, xóa sản phẩm với thông tin chi tiết:
  - Tên sản phẩm
  - Số lượng
  - Giá và khuyến mãi
  - Hình ảnh
  - Mô tả
  - Ngày nhập hàng (tự động cập nhật)
  - Danh mục
- Tìm kiếm sản phẩm theo:
  - Tên sản phẩm
  - Danh mục
  - Khoảng giá
- Quản lý tồn kho tự động
- Upload và quản lý hình ảnh sản phẩm

### 2. Quản lý Đơn hàng
- Theo dõi trạng thái đơn hàng:
  - Chờ xác nhận
  - Đã xác nhận
  - Đang giao hàng
  - Đã giao hàng
  - Đã hủy
- Xử lý đơn hàng:
  - Xác nhận đơn hàng
  - Cập nhật trạng thái giao hàng
  - Hủy đơn hàng
- Xem chi tiết đơn hàng:
  - Thông tin sản phẩm
  - Số lượng
  - Giá
  - Tổng tiền
- Quản lý vận chuyển:
  - Địa chỉ giao hàng
  - Số điện thoại
  - Thời gian đặt hàng

### 3. Giỏ hàng
- Thêm/xóa sản phẩm vào giỏ hàng
- Cập nhật số lượng sản phẩm
- Tính tổng tiền tự động
- Kiểm tra tồn kho khi thêm vào giỏ
- Áp dụng mã giảm giá
- Thanh toán qua PayPal

### 4. Chatbot AI (Dialogflow)
- Tư vấn sản phẩm:
  - Tìm kiếm theo tên
  - Lọc theo danh mục
  - Xem sản phẩm mới
- Kiểm tra giá và khuyến mãi
- Xem thông tin đơn hàng
- Hỏi về thời gian giao hàng
- Tìm kiếm sản phẩm theo danh mục
- Xem sản phẩm mới nhập

### 5. Báo cáo và Thống kê
- Thống kê sản phẩm bán chạy
- Báo cáo theo danh mục
- Thống kê theo thời gian:
  - Theo năm
  - Theo quý
  - Theo tháng
- Báo cáo khách hàng

## Công nghệ sử dụng

### Backend
- Spring Boot 3.3.2
- Spring Security
- Spring Data JPA
- MySQL 8
- Google Dialogflow (AI Chatbot)
- PayPal API
- JWT Authentication

### Frontend
- HTML5
- CSS3
- JavaScript
- jQuery
- Bootstrap
- Thymeleaf

## Cài đặt và Chạy

### Yêu cầu hệ thống
- Java JDK 17 trở lên
- Maven
- MySQL 8
- IDE (Eclipse/IntelliJ IDEA)

### Các bước cài đặt
1. Clone repository:
```bash
git clone https://github.com/your-username/OngVang.git
```

2. Cấu hình database trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ong_vang?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```

3. Cấu hình PayPal (tùy chọn):
```properties
paypal.client.id=your_client_id
paypal.client.secret=your_client_secret
paypal.mode=sandbox
```

4. Cấu hình Dialogflow:
```properties
dialogflow.project-id=your_project_id
dialogflow.credentials-file=path_to_credentials.json
```

5. Build project:
```bash
mvn clean install
```

6. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

## Cấu trúc thư mục
```
src/
├── main/
│   ├── java/
│   │   └── org/example/shoppefood/
│   │       ├── api/          # REST Controllers
│   │       ├── config/       # Cấu hình (Security, PayPal, Dialogflow)
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── entity/       # Entity classes
│   │       ├── mapper/       # Object mappers
│   │       ├── repository/   # Data repositories
│   │       ├── service/      # Business logic
│   │       └── util/         # Utility classes
│   └── resources/
│       ├── static/          # Static resources (CSS, JS, images)
│       └── templates/       # Thymeleaf templates
```

## API Endpoints

### Sản phẩm
- GET `/api/products` - Lấy danh sách sản phẩm
- GET `/api/product/{id}` - Lấy thông tin sản phẩm
- GET `/api/products/{categoryId}` - Lấy sản phẩm theo danh mục
- POST `/api/products` - Thêm sản phẩm mới
- PUT `/api/products/{id}` - Cập nhật sản phẩm
- DELETE `/api/products/{id}` - Xóa sản phẩm

### Đơn hàng
- GET `/api/orders` - Lấy danh sách đơn hàng
- GET `/api/orders/{userId}` - Lấy đơn hàng theo user
- GET `/api/order/{id}` - Lấy chi tiết đơn hàng
- POST `/api/checkout` - Tạo đơn hàng mới
- PUT `/api/order/confirm/{id}` - Xác nhận đơn hàng
- PUT `/api/order/delivered/{id}` - Cập nhật trạng thái giao hàng
- PUT `/api/order/cancel/{id}` - Hủy đơn hàng

### Báo cáo
- GET `/api/reports/products` - Báo cáo sản phẩm bán chạy
- GET `/api/reports/category` - Báo cáo theo danh mục
- GET `/api/reports/years` - Báo cáo theo năm
- GET `/api/reports/months` - Báo cáo theo tháng
- GET `/api/reports/quater` - Báo cáo theo quý
- GET `/api/reports/customer` - Báo cáo khách hàng

### Chatbot
- POST `/api/chatbot/message` - Xử lý tin nhắn từ người dùng

## Bảo mật
- Spring Security với JWT
- Mã hóa mật khẩu với BCrypt
- Xác thực và phân quyền người dùng
- Bảo vệ các endpoint API
- Xử lý session và cookie an toàn

## Đóng góp
Mọi đóng góp đều được hoan nghênh. Vui lòng tạo issue hoặc pull request để đóng góp.

## Giấy phép
Dự án này được cấp phép theo giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.
