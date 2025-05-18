package org.example.shoppefood.service.Cart;

import org.example.shoppefood.config.PayPalConfig;
import org.example.shoppefood.dto.cart.CartItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayPalService {

    @Autowired
    private PayPalConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(config.getClientId(), config.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getBaseUrl() + "/v1/oauth2/token", request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public String createPayPalOrder(List<CartItemDTO> cartItems) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        BigDecimal total = BigDecimal.valueOf(cartItems.stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum());

        Map<String, Object> amount = Map.of(
                "currency_code", "USD",
                "value", total.toString()
        );

        Map<String, Object> purchaseUnit = Map.of("amount", amount);

        Map<String, Object> order = new HashMap<>();
        order.put("intent", "CAPTURE");
        order.put("purchase_units", List.of(purchaseUnit));
        order.put("application_context", Map.of(
                "return_url", config.getSuccessUrl(),
                "cancel_url", config.getCancelUrl()
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(order, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getBaseUrl() + "/v2/checkout/orders", request, Map.class);

        List<Map<String, String>> links = (List<Map<String, String>>) response.getBody().get("links");

        return links.stream()
                .filter(link -> "approve".equals(link.get("rel")))
                .map(link -> link.get("href"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy link thanh toán"));
    }

    public void captureOrder(String orderId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.postForEntity(
                config.getBaseUrl() + "/v2/checkout/orders/" + orderId + "/capture",
                request,
                Map.class
        );
    }
}
