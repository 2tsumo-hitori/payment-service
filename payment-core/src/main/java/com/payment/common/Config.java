package com.payment.common;

import com.payment.common.domain.Address;
import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Stock;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

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
                .stock(Stock.builder().remain(100).build())
                .build());
    }
}
