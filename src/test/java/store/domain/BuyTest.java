package store.domain;

import static org.assertj.core.api.Assertions.*;

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
        Buy buy = new Buy(stock);

        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantitys = List.of(2, 1);
        buy.buyItemByClientInput(products,quantitys);
        Integer testQuantity = buy.getItem().get(Products.findProduct(product));
        assertThat(testQuantity).isEqualTo(quantity);
    }

    @Test
    void 구매수량에따라_지불금액을_계산한다(){
        Stock stock = new Stock();
        Buy buy = new Buy(stock);
        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantity = List.of(2, 1);
        buy.buyItemByClientInput(products,quantity);
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
        Buy buy = new Buy(stock);
        List<Products> products = List.of(Products.CIDER, Products.POTATO_CHIP);
        List<Integer> quantity = List.of(2, 1);
        buy.buyItemByClientInput(products,quantity);

        List<Products> product2 = List.of(Products.COKE);
        List<Integer> quantity2 = List.of(2);
        buy.buyItemByClientInput(product2,quantity2);
        int totalQuantity = buy.getItem().get(Products.CIDER) + buy.getItem().get(Products.POTATO_CHIP) + buy.getItem()
                .get(Products.COKE);
        assertThat(totalQuantity).isEqualTo(5);
    }
}