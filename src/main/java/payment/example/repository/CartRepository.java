package payment.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.example.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
