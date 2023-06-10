package payment.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Cart {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Item item;

    @Builder
    public Cart(Item item) {
        this.item = item;
    }
}
