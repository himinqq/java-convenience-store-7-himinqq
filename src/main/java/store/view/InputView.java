package store.view;

import java.util.function.Supplier;
import store.ErrorMessage;

public class InputView {
    private static final String purchaseStockMessage = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String notifyPromotionMessage = "현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    private final Supplier<String> reader;

    public InputView(Supplier<String> reader) {
        this.reader = reader;
    }

    public String requestPurchaseStock(){
        System.out.println(purchaseStockMessage);
        return reader.get();
    }
    public boolean requestExtraQuantityForApplyPromotion(){
        System.out.println(notifyPromotionMessage);
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
