package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class PromotionDiscountPolicy {

    public enum PromotionAction {
        GIVE_ONE_EXTRA(),
        APPLY_DISCOUNT()
    }

    public boolean isPromotionValidate(Products products, LocalDateTime now) {
        Promotions promotions = products.getPromotions();
        return now.isAfter(promotions.getStart().atStartOfDay())
                && now.isBefore(promotions.getEnd().atTime(23, 59));
    }

    private boolean isSatisfyRequiredQuantity(Products products, int quantity) {
        return requiredQuantity(products) <= quantity;
    }

    public boolean checkPromotionCondition(Products products, int quantity) {
        return isPromotionValidate(products, DateTimes.now()) && isSatisfyRequiredQuantity(products, quantity);
    }

    public int requiredQuantity(Products products) {
        return products.getPromotions().getBuy();
    }

    public boolean needExtraQuantityForPromotion(Products products, int quantity) {
        return requiredQuantity(products) % quantity == requiredQuantity(products)
                || requiredQuantity(products) == quantity;
    }

    public int calculateDiscountAmount(Products products, int quantity) {
        return quantity / (requiredQuantity(products) + 1);
    }

}
