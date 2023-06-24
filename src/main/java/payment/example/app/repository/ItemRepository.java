package payment.example.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import payment.example.common.domain.Item;


import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
}
