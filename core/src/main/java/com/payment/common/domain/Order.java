package com.payment.common.domain;

import jakarta.persistence.*;
import lombok.*;

import static com.payment.common.domain.OrderStatus.ORDER_SUCCESS;
import static com.payment.common.support.validate.PreCondition.*;
import static jakarta.persistence.EnumType.STRING;
import static java.util.Objects.nonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@ToString
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private OrderStatus status = ORDER_SUCCESS;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Item item;

    @Builder
    public Order(OrderStatus status, Member member, Item item) {
        require(nonNull(member));
        require(nonNull(item));

        this.status = status;
        this.member = member;
        this.item = item;
    }

    @Builder
    public Order(Member member, Item item) {
        require(nonNull(member));
        require(nonNull(item));

        this.member = member;
        this.item = item;
    }
}
