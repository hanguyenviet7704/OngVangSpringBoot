package org.example.shoppefood.service;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Value;
import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.repository.ProductRepository;
import org.example.shoppefood.service.impl.DialogflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductChatService {
    private static final Logger logger = LoggerFactory.getLogger(ProductChatService.class);
    private static final int MAX_PRODUCTS_TO_SHOW = 10;

    @Autowired
    private DialogflowService dialogflowService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    public String processMessage(String message, String userId) {
        if (!StringUtils.hasText(message)) {
            return "Xin lỗi, tôi không hiểu yêu cầu của bạn. Vui lòng nhập lại.";
        }

        try {
            DetectIntentResponse response = dialogflowService.detectIntent(message, userId);
            QueryResult queryResult = response.getQueryResult();
            String intent = queryResult.getIntent().getDisplayName();
            Map<String, Value> parameters = queryResult.getParameters().getFieldsMap();

            switch (intent) {
                case "ask_product_price":
                    return handleProductPriceQuery(parameters);
                case "ask_price_range":
                    return handlePriceRangeQuery(parameters);
                case "ask_product_info":
                    return handleProductInfoQuery(parameters);
                case "ask_promotion":
                    return handlePromotionQuery();
                case "ask_shipping":
                    return handleShippingQuery();
                case "ask_order_status":
                    return handleOrderStatusQuery(parameters);
                case "ask_product_by_category":
                    return handleProductByCategory(parameters);
                case "ask_address":
                    return handleAddress();
                case "ask_order_query":
                    return handleOrderQuery();
                case "ask_product_import_time":
                    return handleProductImportTimeQuery(parameters);
                case "ask_new_products":
                    return handleNewProductsQuery();
                default:
                    return queryResult.getFulfillmentText();
            }
        } catch (Exception e) {
            logger.error("Error processing message for user {}: {}", userId, e.getMessage(), e);
            return "Xin lỗi, đã có lỗi xảy ra khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.";
        }
    }

    private String handleAddress(){
        return "Địa chỉ cửa hàng ở thôn Bùi Xá , Xã Nhân Quyền , Huyện Bình Giang , Tỉnh Hải Dương";
    }

    private String handleProductPriceQuery(Map<String, Value> parameters) {
        String productName = getStringParameter(parameters, "product");
        if (!StringUtils.hasText(productName)) {
            return "Bạn muốn biết giá của sản phẩm nào?";
        }

        List<ProductEntity> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        return formatProductListResponse(products, "Giá của sản phẩm (/1kg)", productName);
    }

    private String handlePriceRangeQuery(Map<String, Value> parameters) {
        double minPrice = getNumberParameter(parameters, "min_price");
        double maxPrice = getNumberParameter(parameters, "max_price");

        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return "Xin lỗi, khoảng giá không hợp lệ. Vui lòng nhập lại.";
        }

        List<ProductEntity> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return formatProductListResponse(products, "Các sản phẩm trong khoảng giá", 
            String.format("từ %s đến %s VNĐ", formatPrice(minPrice), formatPrice(maxPrice)));
    }

    private String handleProductInfoQuery(Map<String, Value> parameters) {
        String productName = getStringParameter(parameters, "product");
        if (!StringUtils.hasText(productName)) {
            return "Bạn muốn biết thông tin về sản phẩm nào?";
        }

        List<ProductEntity> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        if (products.isEmpty()) {
            return "Xin lỗi, không tìm thấy sản phẩm \"" + productName + "\".";
        }

        StringBuilder response = new StringBuilder();
        for (int i = 0; i < Math.min(products.size(), MAX_PRODUCTS_TO_SHOW); i++) {
            ProductEntity product = products.get(i);
            response.append(String.format("- %s:\n", product.getProductName()))
                   .append(String.format("  + Giá: %s VNĐ\n", formatPrice(product.getPrice())))
                   .append(String.format("  + Mô tả: %s\n", product.getDescription()))
                   .append(String.format("  + Danh mục: %s\n", product.getCategory().getCategoryName()));
        }

        if (products.size() > MAX_PRODUCTS_TO_SHOW) {
            response.append(String.format("\n... và %d sản phẩm khác.", products.size() - MAX_PRODUCTS_TO_SHOW));
        }

        return response.toString();
    }

    private String handleProductImportTimeQuery(Map<String, Value> parameters) {
        String productName = getStringParameter(parameters, "product");
        if (!StringUtils.hasText(productName)) {
            return "Bạn muốn biết thời gian nhập hàng của sản phẩm nào?";
        }

        List<ProductEntity> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        if (products.isEmpty()) {
            return "Xin lỗi, không tìm thấy sản phẩm \"" + productName + "\".";
        }

        StringBuilder response = new StringBuilder();
        for (int i = 0; i < Math.min(products.size(), MAX_PRODUCTS_TO_SHOW); i++) {
            ProductEntity product = products.get(i);
            response.append(String.format("- %s:\n", product.getProductName()))
                   .append(String.format("  + Thời gian nhập hàng: %s\n", product.getEnteredDate()));
        }

        if (products.size() > MAX_PRODUCTS_TO_SHOW) {
            response.append(String.format("\n... và %d sản phẩm khác.", products.size() - MAX_PRODUCTS_TO_SHOW));
        }

        return response.toString();
    }

    private String handleNewProductsQuery() {
        // Lấy tất cả sản phẩm
        List<ProductEntity> allProducts = productRepository.findAll();
        
        // Lọc sản phẩm được nhập trong năm 2025
        List<ProductEntity> newProducts = allProducts.stream()
            .filter(product -> {
                if (product.getEnteredDate() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(product.getEnteredDate());
                    return cal.get(Calendar.YEAR) == 2025;
                }
                return false;
            })
            .collect(Collectors.toList());

        if (newProducts.isEmpty()) {
            return "Hiện tại chưa có sản phẩm mới nào được nhập trong năm 2025.";
        }

        StringBuilder response = new StringBuilder();
        response.append("Các sản phẩm mới nhập trong năm 2025:\n\n");

        for (int i = 0; i < Math.min(newProducts.size(), MAX_PRODUCTS_TO_SHOW); i++) {
            ProductEntity product = newProducts.get(i);
            response.append(String.format("- %s:\n", product.getProductName()))
                   .append(String.format("  + Giá: %s VNĐ\n", formatPrice(product.getPrice())))
                   .append(String.format("  + Thời gian nhập: %s\n", product.getEnteredDate()));
        }

        if (newProducts.size() > MAX_PRODUCTS_TO_SHOW) {
            response.append(String.format("\n... và %d sản phẩm khác.", newProducts.size() - MAX_PRODUCTS_TO_SHOW));
        }

        return response.toString();
    }

    private String handlePromotionQuery() {
        return "Hiện tại chúng tôi có các chương trình khuyến mãi sau:\n" +
               "1. Giảm giá 20% cho đơn hàng đầu tiên\n" +
               "2. Miễn phí vận chuyển cho đơn hàng trên 500.000 VNĐ\n" +
               "3. Tặng quà cho khách hàng thân thiết";
    }

    private String handleShippingQuery() {
        return "Thông tin vận chuyển:\n" +
               "1. Miễn phí vận chuyển cho đơn hàng trên 500.000 VNĐ\n" +
               "2. Phí vận chuyển 30.000 VNĐ cho đơn hàng dưới 500.000 VNĐ\n" +
               "3. Thời gian giao hàng: 2-3 ngày làm việc";
    }

    private String handleOrderStatusQuery(Map<String, Value> parameters) {
        String orderId = getStringParameter(parameters, "order_id");
        if (!StringUtils.hasText(orderId)) {
            return "Vui lòng cung cấp mã đơn hàng để kiểm tra trạng thái.";
        }
        return "Để kiểm tra trạng thái đơn hàng, vui lòng đăng nhập vào tài khoản của bạn.";
    }
    private String handleOrderQuery() {
        long userID = userService.getCurrentUserId();
        int page = 0;
        int size = 100;
        Pageable pageable = PageRequest.of(page, size);

        ResponsePage<List<OrderDTO>> orders = orderService.getOrdersByUserID(pageable, userID);
        List<OrderDTO> orderList = orders.getContent();

        if (orderList.isEmpty()) {
            return "Bạn chưa có đơn hàng nào.";
        }

        StringBuilder response = new StringBuilder();
        response.append("Danh sách đơn hàng của bạn:\n\n");

        for (OrderDTO order : orderList) {
            Long orderId = order.getOrderId();
            response.append("Đơn hàng #").append(orderId).append(":\n");
            
            try {
                List<PurchasedOrderDTO> orderDetails = productService.findPurchasedProductsByOrderId(orderId);
                if (!orderDetails.isEmpty()) {
                    double totalAmount = 0;
                    for (PurchasedOrderDTO item : orderDetails) {
                        response.append("- ").append(item.getProductName())
                               .append(": ").append(item.getProductQuantity())
                               .append(" x ").append(formatPrice(item.getProductPrice()))
                               .append(" VNĐ\n");
                        totalAmount += item.getProductPrice() * item.getProductQuantity();
                    }
                    response.append("Tổng tiền: ").append(formatPrice(totalAmount)).append(" VNĐ\n");
                }
            } catch (Exception e) {
                logger.error("Error processing order details for order {}: {}", orderId, e.getMessage(), e);
                response.append("Không thể lấy thông tin chi tiết đơn hàng này.\n");
            }
            response.append("\n");
        }

        return response.toString();
    }

    private String handleProductByCategory(Map<String, Value> parameters){
        String category = parameters.get("category").getStringValue();
        if (!StringUtils.hasText(category)) {
            return "Bạn muốn biết thể loại gì shop tôi cung cấp";
        }
        List<ProductEntity> products = productRepository.searchByCategoryName(category);
        return formatProductListResponseByCategory(products,"Chúng tôi có các sản phẩm \n", category);
    }

    private String formatProductListResponseByCategory(List<ProductEntity> products, String prefix, String searchTerm) {
        if (products.isEmpty()) {
            return String.format("Xin lỗi, không tìm thấy sản phẩm phù hợp với loại  \"%s\".", searchTerm);
        }

        StringBuilder response = new StringBuilder();
        response.append(prefix);
        for (ProductEntity product : products) {
            response.append(product.getProductName()).append(",");
        }
        if (response.length() > 2) {
            response.deleteCharAt(response.length() - 1);
        }
        return response.toString();
    }

    private String formatProductListResponse(List<ProductEntity> products, String prefix, String searchTerm) {
        if (products.isEmpty()) {
            return String.format("Xin lỗi, không tìm thấy sản phẩm phù hợp với \"%s\".", searchTerm);
        }

        StringBuilder response = new StringBuilder(String.format("%s \"%s\":\n", prefix, searchTerm));
        for (int i = 0; i < Math.min(products.size(), MAX_PRODUCTS_TO_SHOW); i++) {
            ProductEntity product = products.get(i);
            response.append(String.format("- %s: %s VNĐ\n", 
                product.getProductName(), formatPrice(product.getPrice())));
        }

        if (products.size() > MAX_PRODUCTS_TO_SHOW) {
            response.append(String.format("... và %d sản phẩm khác.", products.size() - MAX_PRODUCTS_TO_SHOW));
        }

        return response.toString();
    }

    private String getStringParameter(Map<String, Value> parameters, String key) {
        Value value = parameters.get(key);
        return value != null ? value.getStringValue() : "";
    }

    private double getNumberParameter(Map<String, Value> parameters, String key) {
        Value value = parameters.get(key);
        return value != null ? value.getNumberValue() : 0.0;
    }

    private String formatPrice(double price) {
        return String.format("%,.0f", price);
    }
}
