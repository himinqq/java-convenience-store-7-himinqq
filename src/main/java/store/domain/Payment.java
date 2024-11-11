package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Payment {
    private int price = 0;
    private int promotionPrice = 0;
    private int membershipPrice = 0;
    private final PromotionDiscountPolicy promotionDiscountPolicy;

    public Payment(PromotionDiscountPolicy promotionDiscountPolicy) {
        this.promotionDiscountPolicy = promotionDiscountPolicy;
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

    public void incrementPromotionPriceForQuantity(Products products, int quantity) {
        IntStream.range(0, quantity)
                .forEach(i -> addPromotionPrice(products.getPrice()));
    }

    public void incrementPriceForQuantity(Products products, int quantity) {
        IntStream.range(0, quantity)
                .forEach(i -> addPrice(products.getPrice()));
    }

    public void applyBuyNGetOneFreeDiscount(Products products, int quantity) {
        if (promotionDiscountPolicy.requiredQuantity(products) != quantity) {
            subtractPrice(products.getPrice() * promotionDiscountPolicy.calculateDiscountCount(products, quantity));
            addPromotionPrice(
                    products.getPrice() * promotionDiscountPolicy.calculateDiscountCount(products, quantity));
        }
    }

    public void cancelPayment(Products products, int quantity) {
        subtractPrice(products.getPrice() * quantity);
    }

    public List<Integer> integratePriceForReceipt() {
        return new ArrayList<>(List.of(promotionPrice, membershipPrice, price));
    }


}
