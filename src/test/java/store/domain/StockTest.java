package store.domain;

import java.util.HashMap;
import java.util.Map.Entry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.view.OutputView;

class StockTest {
    @Test
    void 재고_테스트(){
        Stock stock = new Stock();
        //stock.minusQuantity("콜라",1);

//        for(Entry<Products,Integer>entry : stock.getPromotionStock().entrySet()){
//            if(entry.getKey().equals(Products.COKE))
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }

        OutputView outputView = new OutputView();
        outputView.printStock(stock);

    }
    @Test
    void 고객이_구매하면_재고가_줄어든다(){
        Stock stock = new Stock();
        HashMap<Products, Integer> promotionStock = stock.getPromotionStock();

        Integer before = promotionStock.get(Products.CIDER);
        Buy buy = new Buy("[사이다-2],[감자칩-1]",stock);

        Integer after = promotionStock.get(Products.CIDER);

        Assertions.assertThat(after).isEqualTo(before - 2);
    }

}