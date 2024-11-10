package store.domain;

import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void 프로모션조건을_만족하면_프로모션을_적용하여_지불금액을_계산한다(){
        Payment payment = new Payment(new PromotionDiscountPolicy());
        payment.incrementPriceForQuantity(Products.COKE,2);
    }
}