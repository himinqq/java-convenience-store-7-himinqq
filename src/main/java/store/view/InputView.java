package store.view;

import java.util.function.Supplier;
import store.ErrorMessage;

public class InputView {
    private static final String purchaseStockMessage = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";


    private final Supplier<String> reader;

    public InputView(Supplier<String> reader) {
        this.reader = reader;
    }

    public String requestPurchaseStock(){
        System.out.println(purchaseStockMessage);
        return reader.get();
    }


}
