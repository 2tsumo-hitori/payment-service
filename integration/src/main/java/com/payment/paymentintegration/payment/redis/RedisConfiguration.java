package com.payment.paymentintegration.payment.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisKeyConfig redisKeyConfig;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    @ConfigurationProperties("redis")
    public RedisKeyConfig redisKeyConfig() {
        return new RedisKeyConfig();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisKeyConfig.getHost() + ":" + redisKeyConfig.getPort());
        RedissonClient redisson  = Redisson.create(config);

        return redisson;
    }
}
