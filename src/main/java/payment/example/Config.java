package payment.example;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    @ConfigurationProperties("payment")
    public KeyConfig keyConfig() {
        return new KeyConfig();
    }

    @Bean
    public IamportClient iamportClient(KeyConfig keyConfig) {
        return new IamportClient(keyConfig.getApiKey(), keyConfig.getApiSecret());
    }
}
