package payment.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payment.example.repository.dto.OrderResponse;
import payment.example.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select new payment.example.repository.dto.OrderResponse(o.id, o.status, m.id, m.name, i.id, i.name)" +
            " from Order o" +
            " join o.member m" +
            " join o.item i" +
            " where o.id = :orderId")
    OrderResponse findOrder(@Param("orderId") Long orderId);
}
