package payment.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.EnumType.*;
import static payment.example.domain.OrderStatus.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@ToString
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private OrderStatus status = 주문_성공;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Item item;

    public Order(OrderStatus status, Member member, Item item) {
        this.status = status;
        this.member = member;
        this.item = item;
    }

    public Order(Member member, Item item) {
        this.member = member;
        this.item = item;
    }

    public static Order create(Member member, Item item) {
        return new Order(member, item);
    }

    public static Order create(OrderStatus status,Member member, Item item) {
        return new Order(status, member, item);
    }
}
