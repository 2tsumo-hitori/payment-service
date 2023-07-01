package com.payment.common.repository;

import com.payment.common.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.common.domain.Stock.*;


@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    Item item;

    @Test
    void 상품_이름으로_찾기_성공() {
        itemRepository.save(Item.builder()
                .name("상품")
                .price(100)
                .stock(builder().remain(100).build())
                .build());

        item = itemRepository.findByName("상품").orElseThrow();

        Assertions.assertThat(item).isNotNull();
    }
}