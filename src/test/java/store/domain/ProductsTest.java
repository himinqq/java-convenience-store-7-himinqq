package store.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductsTest {
    @Test
    void 고객이구매한_수량만큼_재고에서_차감된다() {
        Stock stock = new Stock();

        Integer before = stock.getPromotionStock().get(Products.COKE);
        stock.decreasePromotionStock(Products.COKE);
        Integer after = stock.getPromotionStock().get(Products.COKE);

        Assertions.assertThat(before - after).isEqualTo(1);

    }
}