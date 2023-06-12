package payment.example.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import payment.example.domain.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    Item item;

    @Test
    void 상품_이름으로_찾기_성공() {
        item = itemRepository.findByName("상품1").orElseThrow();

        Assertions.assertThat(item).isNotNull();
    }
}