package payment.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payment.example.common.domain.Order;
import payment.example.app.repository.dto.OrderResponse;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select new payment.example.app.repository.dto.OrderResponse(o.id, o.status, m.id, m.name, i.id, i.name, i.stock)" +
            " from Order o" +
            " join o.member m" +
            " join o.item i" +
            " where o.id = :orderId")
    OrderResponse findOrder(@Param("orderId") Long orderId);
}
