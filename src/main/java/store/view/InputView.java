package store.view;

import java.util.function.Supplier;
import store.ErrorMessage;

public class InputView {
    private static final String purchaseStockMessage = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String notifyPromotionMessage = "현재 %s 은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String notifyOutOfStockNoPromotionMessage = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String membershipApplyMessage = "멤버십 할인을 받으시겠습니까? (Y/N)";

    private final Supplier<String> reader;

    public InputView(Supplier<String> reader) {
        this.reader = reader;
    }

    public String requestPurchaseStock(){
        System.out.println(purchaseStockMessage);
        return reader.get();
    }
    public boolean requestExtraQuantityForApplyPromotion(String name){
        System.out.printf((notifyPromotionMessage) + "%n",name);
        String answer = reader.get();
        validateYesOrNoAnswer(answer);
        return answer.equals("Y");
    }
    public boolean requestApplyOutOfStockNoPromotion(String name, int quantity){
        System.out.printf((notifyOutOfStockNoPromotionMessage) + "%n",name,quantity);
        String answer = reader.get();
        validateYesOrNoAnswer(answer);
        return answer.equals("Y");
    }
    public boolean requestApplyMembershipDiscount(){
        System.out.println(membershipApplyMessage);
        String answer = reader.get();
        validateYesOrNoAnswer(answer);
        return answer.equals("Y");
    }

    private void validateYesOrNoAnswer(String answer){
        if(!answer.equals("Y") && !answer.equals("N")){
            throw new IllegalArgumentException(ErrorMessage.YES_OR_NO_ANSWER_ERROR.getMessage());
        }
    }


}
