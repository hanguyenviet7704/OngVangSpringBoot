package org.example.shoppefood.config;


import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.repository.ProductRepository;
import org.example.shoppefood.service.impl.DialogflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductChatService {

    @Autowired
    private DialogflowService dialogflowService;

    @Autowired
    private ProductRepository productRepository;

    public String processMessage(String message) {
        try {
            // Gửi message tới Dialogflow để phân tích
            DetectIntentResponse response = dialogflowService.detectIntent(message);
            QueryResult queryResult = response.getQueryResult();

            // Lấy intent được phát hiện
            String intent = queryResult.getIntent().getDisplayName();

            // Xử lý dựa trên intent
            switch (intent) {
                case "ask_product_price":
                    // Lấy tên sản phẩm từ tham số
                    String productName = queryResult.getParameters().getFieldsOrDefault(
                            "product",
                            com.google.protobuf.Value.getDefaultInstance()).getStringValue();

                    if (productName != null && !productName.isEmpty()) {
                        return getProductPriceResponse(productName);
                    } else {
                        return "Bạn muốn biết giá của sản phẩm nào?";
                    }

                case "ask_price_range":
                    // Xử lý hỏi về khoảng giá
                    double minPrice = queryResult.getParameters().getFieldsOrDefault(
                            "min_price",
                            com.google.protobuf.Value.getDefaultInstance()).getNumberValue();
                    double maxPrice = queryResult.getParameters().getFieldsOrDefault(
                            "max_price",
                            com.google.protobuf.Value.getDefaultInstance()).getNumberValue();

                    return getProductsByPriceRange(minPrice, maxPrice);

                default:
                    // Sử dụng phản hồi mặc định từ Dialogflow
                    return queryResult.getFulfillmentText();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này.";
        }
    }

    private String getProductPriceResponse(String productName) {
        List<ProductEntity> products = productRepository.findByProductNameContainingIgnoreCase(productName);

        if (products.isEmpty()) {
            return "Xin lỗi, chúng tôi không tìm thấy sản phẩm có tên \"" + productName + "\".";
        } else if (products.size() == 1) {
            ProductEntity product = products.get(0);
            return "Giá của sản phẩm \"" + product.getProductName() + "\" là "
                    + formatPrice(product.getPrice()) + " VNĐ.";
        } else {
            StringBuilder response = new StringBuilder("Chúng tôi có các sản phẩm sau phù hợp với tìm kiếm của bạn:\n");

            for (int i = 0; i < Math.min(products.size(), 5); i++) {
                ProductEntity product = products.get(i);
                response.append("- ").append(product.getProductName())
                        .append(": ").append(formatPrice(product.getPrice())).append(" VNĐ\n");
            }

            if (products.size() > 5) {
                response.append("... và ").append(products.size() - 5).append(" sản phẩm khác.");
            }

            return response.toString();
        }
    }

    private String getProductsByPriceRange(double minPrice, double maxPrice) {
        List<ProductEntity> products = productRepository.findByPriceBetween(minPrice, maxPrice);

        if (products.isEmpty()) {
            return "Xin lỗi, chúng tôi không tìm thấy sản phẩm nào trong khoảng giá từ "
                    + formatPrice(minPrice) + " đến " + formatPrice(maxPrice) + " VNĐ.";
        } else {
            StringBuilder response = new StringBuilder("Các sản phẩm trong khoảng giá từ "
                    + formatPrice(minPrice) + " đến " + formatPrice(maxPrice) + " VNĐ:\n");

            for (int i = 0; i < Math.min(products.size(), 5); i++) {
                ProductEntity product = products.get(i);
                response.append("- ").append(product.getProductName())
                        .append(": ").append(formatPrice(product.getPrice())).append(" VNĐ\n");
            }

            if (products.size() > 5) {
                response.append("... và ").append(products.size() - 5).append(" sản phẩm khác.");
            }

            return response.toString();
        }
    }

    private String formatPrice(double price) {
        return String.format("%,.0f", price);
    }
}
