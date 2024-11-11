package store.domain;

import java.util.Arrays;

public enum Products {
    COKE("콜라", 1000, Promotions.SPARKLE_TWO_PULUS_ONE),
    CIDER("사이다", 1000, Promotions.SPARKLE_TWO_PULUS_ONE),
    ORANGE_JUICE("오렌지주스", 1800, Promotions.MD_RECOMMEND),
    SPARKLING_WATER("탄산수", 1200, Promotions.SPARKLE_TWO_PULUS_ONE),
    WATER("물", 500, Promotions.NO_PROMOTION),
    VITAMIN_WATER("비타민워터", 1500, Promotions.NO_PROMOTION),
    POTATO_CHIP("감자칩", 1500, Promotions.TIME_SALE),
    CHOCO_BAR("초코바", 1200, Promotions.MD_RECOMMEND),
    ENERGY_BAR("에너지바", 2000, Promotions.NO_PROMOTION),
    MEAL_BOX("정식도시락", 6400, Promotions.NO_PROMOTION),
    CUP_RAMEN("컵라면", 1700, Promotions.MD_RECOMMEND),
    NOTHING("", 0, Promotions.NO_PROMOTION),
    ;

    private final String name;
    private final int price;
    private final Promotions promotions;

    Products(String name, int price, Promotions promotions) {
        this.name = name;
        this.price = price;
        this.promotions = promotions;
    }

    public int getPrice() {
        return price;
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public String getName() {
        return name;
    }

    public static Products findProduct(String name) {
        return Arrays.stream(Products.values())
                .filter(p -> p.getName().equals(name))
                .findFirst().orElse(NOTHING);
    }

    public static boolean isPromotionProduct(String name) {
        return !findProduct(name).getPromotions().equals(Promotions.NO_PROMOTION);
    }

}
