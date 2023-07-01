package com.payment.paymentintegration.payment.iamport;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamPortConfiguration {
    @Bean
    @ConfigurationProperties("payment")
    public IamPortKeyConfig keyConfig() {
        return new IamPortKeyConfig();
    }

    @Bean
    public IamportClient iamportClient(IamPortKeyConfig keyConfig) {
        return new IamportClient(keyConfig.getApiKey(), keyConfig.getApiSecret());
    }
}
