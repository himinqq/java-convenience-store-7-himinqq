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
    void 고객이_구매하면_재고가_줄어든다(){
        Stock stock = new Stock();
        HashMap<Products, Integer> promotionStock = stock.getPromotionStock();

        Integer before = promotionStock.get(Products.CIDER);

        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantity = List.of(2, 1);
        HashMap<Products, Integer> item = new HashMap<>();
        for(int i =0; i<quantity.size(); i++){
            item.put(products.get(i), quantity.get(i));
        }
        Buy buy = new Buy(stock, item);
        buy.getItem().entrySet().forEach(i->{
            int remain = stock.decreasePromotionStockAndReturnRemaining(i.getKey(), i.getValue());
            if(remain != 0) stock.decreaseQuantityAtNoPromotionStock(i.getKey(),i.getValue());
        });

        Integer after = promotionStock.get(Products.CIDER);

        Assertions.assertThat(after).isEqualTo(before - 2);
    }

    @Test
    void 수량을_초과하면_에러가발생한다(){
        Stock stock = new Stock();

        Assertions.assertThatThrownBy(()->
                stock.checkExceedQuantity(stock.getPromotionStock(), Products.COKE, 12))
                .isInstanceOf(IllegalArgumentException.class);

    }

}