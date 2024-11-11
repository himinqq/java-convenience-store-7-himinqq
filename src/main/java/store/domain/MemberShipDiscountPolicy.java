package store.domain;

public class MemberShipDiscountPolicy {
    public int discountAmount(int promotionTotalPrice, int promotionDiscountPrice){
        return (int) ((promotionTotalPrice+promotionDiscountPrice) * 0.3);
    }

    public boolean isExceedMembershipDiscountLimit(double discountAmount){
        return discountAmount > 8000;
    }

}
