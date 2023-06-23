package payment.example.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.*;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.*;
import static payment.example.support.validate.PreCondition.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private int point;

    private static final int MIN_VALUE = 0;

    @Builder
    public Member(String name, Address address, int point) {
        require(isNotBlank(name));
        require(nonNull(address));
        require(point > MIN_VALUE);

        this.name = name;
        this.address = address;
        this.point = point;
    }
}
