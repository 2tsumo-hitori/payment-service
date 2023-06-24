package payment.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long remain;

    @Builder
    public Stock(final long remain) {
        this.remain = remain;
    }

    public void decrease(final long quantity) {
        if (this.remain - quantity < 0) {
            throw new RuntimeException("재고 부족");
        }
        this.remain -= quantity;
    }
}
