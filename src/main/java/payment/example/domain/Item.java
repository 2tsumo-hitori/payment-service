package payment.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Long stock;

    private int price;

    public void decreaseItemStock() {
        this.stock -= 1;
    }

    @Builder
    public Item(String name, Long stock, int price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }
}
