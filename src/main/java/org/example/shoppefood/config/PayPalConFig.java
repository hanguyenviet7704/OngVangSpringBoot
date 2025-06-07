package org.example.shoppefood.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConFig {
    private static final Logger logger = LoggerFactory.getLogger(PayPalConFig.class);

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("mode", mode);

        return config;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        logger.info("Creating PayPal API Context");
        OAuthTokenCredential credential = oAuthTokenCredential();
        String accessToken = credential.getAccessToken();
        logger.debug("Access Token: {}", accessToken != null ? "****" : "null");
        
        APIContext context = new APIContext(accessToken);
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
