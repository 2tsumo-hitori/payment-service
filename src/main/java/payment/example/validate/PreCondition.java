package payment.example.validate;

import payment.example.exception.ItemStatusException;
import payment.example.exception.OutOfStockException;

public class PreCondition {
    public static void itemPriceValidate(boolean expression) {
        if (!expression) {
            throw new ItemStatusException("상품 가격이 일치하지 않습니다.");
        }
    }

    public static void itemNameValidate(boolean expression) {
        if (!expression) {
            throw new ItemStatusException("상품 이름이 일치하지 않습니다.");
        }
    }

    public static void itemStockValidate(boolean expression) {
        if (!expression) {
            throw new OutOfStockException("상품 재고가 부족합니다.");
        }
    }

    public static void itemExistValidate(boolean expression) {
        if (!expression) {
            throw new ItemStatusException("존재하지 않는 상품입니다.");
        }
    }

    public static void require(boolean expression) {
        if(!expression) {
            throw new IllegalArgumentException();
        }
    }
}
