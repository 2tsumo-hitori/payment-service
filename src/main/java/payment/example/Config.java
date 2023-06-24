package payment.example;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import payment.example.common.domain.Address;
import payment.example.common.domain.Item;
import payment.example.common.domain.Member;
import payment.example.app.repository.ItemRepository;
import payment.example.app.repository.MemberRepository;
import payment.example.common.support.aop.LogTraceAspect;
import payment.example.common.support.logtrace.LogTrace;

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

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @EventListener(ApplicationReadyEvent.class)
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
                .stock(100L)
                .build());
    }
}
