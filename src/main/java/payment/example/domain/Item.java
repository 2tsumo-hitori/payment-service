package payment.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long stock;

    private Long price;

    @Builder
    public Item(String name, Long stock, Long price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }
}
