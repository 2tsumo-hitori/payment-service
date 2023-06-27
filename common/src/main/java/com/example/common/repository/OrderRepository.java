package com.example.common.repository;

import com.example.common.repository.dto.GetOrderDto;
import com.example.common.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select new com.example.common.repository.dto.GetOrderDto(o.id, m.id, m.name, i.id, i.name)" +
            " from Order o" +
            " join o.member m" +
            " join o.item i" +
            " where o.id = :orderId")
    GetOrderDto findOrder(@Param("orderId") Long orderId);
}
