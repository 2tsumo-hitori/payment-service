package payment.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.common.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select new payment.example.app.repository.dto.GetOrderDto(o.id, m.id, m.name, i.id, i.name)" +
            " from Order o" +
            " join o.member m" +
            " join o.item i" +
            " where o.id = :orderId")
    GetOrderDto findOrder(@Param("orderId") Long orderId);
}
