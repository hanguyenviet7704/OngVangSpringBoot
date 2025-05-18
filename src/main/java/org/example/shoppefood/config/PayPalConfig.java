package org.example.shoppefood.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paypal")
@Getter
@Setter
public class PayPalConfig {
    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String successUrl;
    private String cancelUrl;
}
