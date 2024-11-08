package store.domain;

import java.time.LocalDateTime;

public class PromotionDiscount {

    public boolean isPromotionValidate(Products products, LocalDateTime now){
        Promotions promotions = products.getPromotions();
        return now.isAfter(promotions.getStart().atStartOfDay())
                && now.isBefore(promotions.getEnd().atTime(23,59));
    }

    public boolean isRequiredQuantity(Products products, int quantity){
        int requiredQuantity = products.getPromotions().getBuy();
        return requiredQuantity == quantity;
    }
}
