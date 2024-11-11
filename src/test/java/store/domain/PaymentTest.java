package store.domain;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PaymentTest {
    static Stream<Arguments> purchase() {
        return Stream.of(
                Arguments.of( Map.of(Products.COKE, 3, Products.ENERGY_BAR, 5))
        );
    }

    @Test
    void 프로모션조건을_만족하면_프로모션을_적용하여_지불금액을_계산한다(){
        Payment payment = new Payment(new PromotionDiscountPolicy());
        payment.incrementPriceForQuantity(Products.COKE,2);
    }

    @ParameterizedTest
    @MethodSource("purchase")
    void 멤버십할인을_적용하여_계산한다(Map<Products,Integer> item){

        Buy buy = new Buy(new Stock(),item);
        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();
        Payment payment = new Payment(promotionDiscountPolicy);
        buy.getItem().forEach(payment::incrementPriceForQuantity);

        for(Entry<Products,Integer> entry : buy.getItem().entrySet()){
            Products product = entry.getKey();
            Integer quantity = entry.getValue();
            if(Products.isPromotionProduct(product.getName())){
                payment.applyBuyNGetOneFreeDiscount(product,quantity);
                buy.addDisCountItem(product,
                        promotionDiscountPolicy.calculateDiscountCount(product,quantity));
            }
        }
        payment.applyMembershipDiscount(buy,new MemberShipDiscountPolicy());

        int membershipPrice = payment.getMembershipPrice();


        Assertions.assertThat(membershipPrice).isEqualTo(3000);
    }
}