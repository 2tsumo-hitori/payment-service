package payment.example.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private int point;

    @OneToMany
    private List<Cart> carts = new ArrayList<>();

    @Builder
    public Member(String name, Address address, int point) {
        this.name = name;
        this.address = address;
        this.point = point;
    }
}
