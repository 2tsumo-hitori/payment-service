package payment.example;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payment.example.domain.Address;
import payment.example.domain.Item;
import payment.example.domain.Member;
import payment.example.domain.Stock;
import payment.example.repository.ItemRepository;
import payment.example.repository.MemberRepository;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Bean
    @ConfigurationProperties("payment")
    public KeyConfig keyConfig() {
        return new KeyConfig();
    }

    @Bean
    public IamportClient iamportClient(KeyConfig keyConfig) {
        return new IamportClient(keyConfig.getApiKey(), keyConfig.getApiSecret());
    }

    @PostConstruct
    public void init() {
        memberRepository.save(Member.builder()
                .name("고객")
                .point(100)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build());

        itemRepository.save(Item.builder()
                .name("상품1")
                .price(100)
                .stock(Stock.builder().remain(100).build())
                .build());
    }
}
