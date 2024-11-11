package store.domain;

public class MemberShipDiscountPolicy {
    public static final int MAXIMUM_DISCOUNT_PRICE = 8000;

    public int discountAmount(int promotionTotalPrice, int promotionDiscountPrice){
        return (int) ((promotionTotalPrice+promotionDiscountPrice) * 0.3);
    }

    public boolean isExceedMembershipDiscountLimit(double discountAmount){
        return discountAmount > MAXIMUM_DISCOUNT_PRICE;
    }

}
