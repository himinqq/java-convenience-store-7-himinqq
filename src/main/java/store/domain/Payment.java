package store.domain;

import java.util.Map.Entry;
import java.util.stream.IntStream;

public class Payment {
    private int price = 0;
    private int promotionPrice = 0;
    private final PromotionDiscountPolicy promotionDiscountPolicy;

    public Payment(PromotionDiscountPolicy promotionDiscountPolicy) {
        this.promotionDiscountPolicy = promotionDiscountPolicy;
    }

    public int getPromotionPrice() {
        return promotionPrice;
    }

    public int getPrice() {
        return price;
    }

    private void subtractPrice(int price) {
        this.price -= price;
    }

    private void addPrice(int price) {
        this.price += price;
    }

    private void addPromotionPrice(int price) {
        this.promotionPrice += price;
    }

    public void incrementPriceForQuantity(Products products, int quantity) {
        IntStream.range(0, quantity)
                .forEach(i -> addPrice(products.getPrice()));
    }

    public void applyPromotion(Buy buy, Products product, Integer quantity, boolean extraQuantityAnswer) {
        if (promotionDiscountPolicy.checkPromotionCondition(product, quantity)) {
            applyBuyNGetOneFreeDiscount(product, quantity);
            applyPromotionForExtraQuantity(buy, extraQuantityAnswer, product, quantity);
        }
    }

    private void applyPromotionForExtraQuantity(Buy buy, boolean extraQuantityAnswer, Products product,
                                                Integer quantity) {
        if (promotionDiscountPolicy.needExtraQuantityForPromotion(product, quantity)) {
            if (extraQuantityAnswer) {
                buy.getOneFree(product);
            }
        }
    }

    public void applyBuyNGetOneFreeDiscount(Products products, int quantity) {
        if (promotionDiscountPolicy.requiredQuantity(products) != quantity) {
            subtractPrice(products.getPrice() * promotionDiscountPolicy.calculateDiscountAmount(products, quantity));
            addPromotionPrice(
                    products.getPrice() * promotionDiscountPolicy.calculateDiscountAmount(products, quantity));
        }
    }

}
