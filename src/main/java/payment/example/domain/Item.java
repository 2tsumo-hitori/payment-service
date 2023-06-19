package payment.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.*;
import static org.apache.logging.log4j.util.Strings.*;
import static payment.example.validate.PreCondition.*;

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

    private static final int MIN_VALUE = 0;

    public void decreaseItemStock() {
        this.stock -= 1;
    }

    @Builder
    public Item(String name, Long stock, int price) {
        require(isNotBlank(name));
        require(price > MIN_VALUE);

        this.name = name;
        this.stock = stock;
        this.price = price;
    }
}
