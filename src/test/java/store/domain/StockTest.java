package store.domain;

import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StockTest {
    @Test
    void 재고_테스트(){
        Stock stock = new Stock();
        HashMap<Products, Integer> promotionStock = stock.getPromotionStock();
        Assertions.assertThat(promotionStock.get(Products.CIDER)).isEqualTo(8);


    }
    @Test
    void 수량을_초과하면_에러가발생한다(){
        Stock stock = new Stock();

        Assertions.assertThatThrownBy(()->
                stock.checkExceedQuantity(stock.getPromotionStock(), Products.COKE, 12))
                .isInstanceOf(IllegalArgumentException.class);

    }

}