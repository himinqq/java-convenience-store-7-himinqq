package store.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import store.domain.Products;
import store.domain.Stock;

public class OutputView {
    private final String welcomeMessage = "안녕하세요. W편의점입니다.";
    private final String stockNotifyMessage = "현재 보유하고 있는 상품입니다.\n";
    private final String productMessage = "- %s %s원 %s";
    private final String promotionTypeMessage = " %s";
    private final String outOfStockMessage = "재고 없음";

    private final String BUY_ITEM_MESSAGE = "%s\t\t%d \t%,d";
    private final String FREE_ITEM_MESSAGE = "%s\t\t%d";
    private final String TOTAL_PRICE_MESSAGE = "총구매액\t\t%d\t%,d\n";
    private final String MEMBERSHIP_DISCOUNT_MESSAGE = "멤버십할인\t\t\t-%d\n";
    private final String PROMOTION_DISCOUNT_MESSAGE = "행사할인\t\t\t-%,d\n";

    private final String MUST_PAYMENT = "내실돈\t\t\t %,d";

    public void printWelcome() {
        System.out.println(welcomeMessage);
    }

    public void printStock(Stock stock) {
        System.out.println(stockNotifyMessage);
        HashMap<Products, Integer> promotionStock = stock.getPromotionStock();
        HashMap<Products, Integer> noPromotionStock = stock.getNoPromotionStock();

        for (Products products : Products.values()) {
            if (products.equals(Products.NOTHING)) {
                continue;
            }
            String name = products.getName();
            String price = String.format("%,d", products.getPrice());
            if (Products.isPromotionProduct(products.getName())) {
                String promotionProductMessage = String.format(productMessage + promotionTypeMessage, name, price,
                        convertStockMessage(promotionStock.get(products)), products.getPromotions().getName());
                System.out.println(promotionProductMessage);
            }
            String noPromotionProductMessage = String.format(productMessage, name, price,
                    convertStockMessage(noPromotionStock.get(products)));
            System.out.println(noPromotionProductMessage);
        }
    }

    private String convertStockMessage(Integer stock) {
        if (stock == null || stock == 0) {
            return outOfStockMessage;
        }
        return String.valueOf(stock) + "개";
    }

    public void printReceipt(Map<Products, Integer> buyItem, Map<Products, Integer> freeItem, ArrayList<Integer> payment) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("===========W 편의점=============\n 상품명\t\t수량\t금액\n");

        buyItem.entrySet().forEach(i -> {
            receipt.append(String.format(BUY_ITEM_MESSAGE, i.getKey().getName(), i.getValue(), i.getValue() * i.getKey().getPrice()))
                    .append("\n");
        });

        receipt.append("===========증\t정=============\n");
        freeItem.entrySet().forEach(i -> {
            receipt.append(String.format(FREE_ITEM_MESSAGE, i.getKey().getName(), i.getValue()))
                    .append("\n");
        });

        receipt.append("==============================\n");

        int totalQuantity = buyItem.entrySet().stream().mapToInt(i -> i.getValue()).sum();
        int totalPrice = buyItem.entrySet().stream().mapToInt(i -> i.getKey().getPrice() * i.getValue()).sum();
        receipt.append(String.format(TOTAL_PRICE_MESSAGE, totalQuantity, totalPrice));

        receipt.append(String.format(PROMOTION_DISCOUNT_MESSAGE, payment.get(0)))
                .append(String.format(MEMBERSHIP_DISCOUNT_MESSAGE, payment.get(1)))
                .append(String.format(MUST_PAYMENT, payment.get(2)));

        System.out.println(receipt);
    }


}
