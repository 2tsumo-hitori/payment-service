package payment.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import payment.example.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
