package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.controller.ConvenienceController;


class BuyTest {

    static Stream<Arguments> purchase() {
        return Stream.of(
                Arguments.of("사이다", 2),
                Arguments.of("감자칩", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("purchase")
    void 사용자가입력한_구매상품과수량을_구분하여_저장한다(String product, int quantity) {
        Stock stock = new Stock();


        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantitys = List.of(2, 1);
        HashMap<Products, Integer> item = new HashMap<>();
        for(int i =0; i<quantitys.size(); i++){
            item.put(products.get(i), quantitys.get(i));
        }
        Buy buy = new Buy(stock,item);

        Integer testQuantity = buy.getItem().get(Products.findProduct(product));
        assertThat(testQuantity).isEqualTo(quantity);
    }

    @Test
    void 구매수량에따라_지불금액을_계산한다(){
        Stock stock = new Stock();

        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantity = List.of(2, 1);
        HashMap<Products, Integer> item = new HashMap<>();
        for(int i =0; i<quantity.size(); i++){
            item.put(products.get(i), quantity.get(i));
        }
        Buy buy = new Buy(stock, item);
        Payment payment = new Payment(new PromotionDiscountPolicy());
        for(Entry<Products,Integer> entry : buy.getItem().entrySet()){
            payment.incrementPriceForQuantity(entry.getKey(),entry.getValue());
        }
        int expectedPrice = Products.CIDER.getPrice()*2 + Products.POTATO_CHIP.getPrice();
        assertThat(payment.getPrice()).isEqualTo(expectedPrice);
    }
    @Test
    void 사용자가_제품구매이후_추가구매를_진행한다(){
        Stock stock = new Stock();

        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantity = List.of(2, 1);
        HashMap<Products, Integer> item = new HashMap<>();
        for(int i =0; i<quantity.size(); i++){
            item.put(products.get(i), quantity.get(i));
        }
        Buy buy = new Buy(stock, item);

        List<Products> product2 = List.of(Products.COKE);
        List<Integer> quantity2 = List.of(2);
        HashMap<Products, Integer> item2 = new HashMap<>();
        for(int i =0; i<quantity2.size(); i++){
            item.put(product2.get(i), quantity2.get(i));
        }
        buy.addBuyItem(item2);

        int totalQuantity = buy.getItem().get(Products.CIDER) + buy.getItem().get(Products.POTATO_CHIP) + buy.getItem()
                .get(Products.COKE);

        assertThat(totalQuantity).isEqualTo(5);
    }
}