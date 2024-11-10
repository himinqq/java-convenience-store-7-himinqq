package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionDiscountPolicyTest {
    @Test
    void 제품의_프로모션기간이_유효한지확인한다(){
        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();

        LocalDateTime nowTestTime = LocalDate.of(2024,2,1).atStartOfDay();
        boolean promotionValidate = promotionDiscountPolicy.isPromotionValidate(Products.POTATO_CHIP,nowTestTime);

        Assertions.assertThat(promotionValidate).isEqualTo(false);
    }
}