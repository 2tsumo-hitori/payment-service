package payment.example.common.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import payment.example.common.support.validate.PreCondition;

import static java.util.Objects.*;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.*;

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
        PreCondition.require(isNotBlank(name));
        PreCondition.require(nonNull(address));
        PreCondition.require(point > MIN_VALUE);

        this.name = name;
        this.address = address;
        this.point = point;
    }
}
