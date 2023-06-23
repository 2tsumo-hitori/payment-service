package payment.example.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.*;
import static java.util.Objects.*;
import static payment.example.domain.OrderStatus.*;
import static payment.example.support.validate.PreCondition.*;

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
