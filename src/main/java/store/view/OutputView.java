package store.view;

import java.util.HashMap;
import store.domain.Products;
import store.domain.Stock;

public class OutputView {
    private final String welcomeMessage = "안녕하세요. W편의점입니다.";
    private final String stockNotifyMessage = "현재 보유하고 있는 상품입니다.\n";
    private final String productMessage = "- %s %d원 %s";
    private final String promotionTypeMessage = " %s";
    private final String outOfStockMessage = "재고없음";


    public void printWelcome(){
        System.out.println(welcomeMessage);
    }

    public void printStock(Stock stock){
        System.out.println(stockNotifyMessage);
        HashMap<Products, Integer> promotionStock = stock.getPromotionStock();
        HashMap<Products, Integer> noPromotionStock = stock.getNoPromotionStock();

        for(Products products : Products.values()){
            if(products.equals(Products.NOTHING)) continue;
            String name = products.getName();
            int price = products.getPrice();
            if(Products.isPromotionProduct(products.getName())) {
                String promotionProductMessage = String.format(productMessage + promotionTypeMessage, name, price,
                        convertStockMessage(promotionStock.get(products)), products.getPromotions().getName());
                System.out.println(promotionProductMessage);
            }
            String noPromotionProductMessage = String.format(productMessage, name, price,
                    convertStockMessage(noPromotionStock.get(products)));
            System.out.println(noPromotionProductMessage);
        }
    }
    private String convertStockMessage(Integer stock){
        if(stock == null || stock == 0) return outOfStockMessage;
        return String.valueOf(stock)+"개";
    }

}
