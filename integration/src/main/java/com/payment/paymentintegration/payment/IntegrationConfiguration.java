package com.payment.paymentintegration.payment;

import com.payment.paymentintegration.payment.iamport.KeyConfig;
import com.payment.paymentintegration.payment.redis.RedisKeyConfig;
import com.siot.IamportRestClient.IamportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationConfiguration {
    @Bean
    @ConfigurationProperties("payment")
    public KeyConfig keyConfig() {
        return new KeyConfig();
    }

    @Bean
    public IamportClient iamportClient(KeyConfig keyConfig) {
        return new IamportClient(keyConfig.getApiKey(), keyConfig.getApiSecret());
    }

    @Bean
    @ConfigurationProperties("redis")
    public RedisKeyConfig redisKeyConfig() {
        return new RedisKeyConfig();
    }
}
