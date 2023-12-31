package com.payment.common.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.payment.common.support.validate.PreCondition.itemStockValidate;
import static com.payment.common.support.validate.PreCondition.require;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Stock stock;

    private int price;

    private static final int MIN_VALUE = 0;

    private static final int MIN_STACK = 0;

    @Builder
    public Item(String name, Stock stock, int price) {
        require(isNotBlank(name));
        require(price > MIN_VALUE);

        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public long getQuantity() {
        return stock.getRemain();
    }

    public Long getStockId() {
        return stock.getId();
    }

    public void purchase(final long quantity) {
        itemStockValidate(getQuantity() > MIN_STACK);

        stock.decrease(quantity);
    }
}
