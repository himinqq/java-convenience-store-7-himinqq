package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class PromotionDiscount {

    public boolean isPromotionValidate(Products products, LocalDateTime now){
        Promotions promotions = products.getPromotions();
        return now.isAfter(promotions.getStart().atStartOfDay())
                && now.isBefore(promotions.getEnd().atTime(23,59));
    }

    public boolean isSatisfyRequiredQuantity(Products products, int quantity){
        int requiredQuantity = products.getPromotions().getBuy();
        return requiredQuantity <= quantity;
    }

    public boolean checkPromotionCondition(Products products, int quantity) {
        return isPromotionValidate(products, DateTimes.now()) && isSatisfyRequiredQuantity(products, quantity);
    }
}
