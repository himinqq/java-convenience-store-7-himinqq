package store.domain;

import java.util.HashMap;
import java.util.Map;

public class Buy {
    private Map<Products, Integer> item = new HashMap<>();
    private final Map<Products, Integer> freeItem = new HashMap<>();
    private final Stock stock;

    public Buy(Stock stock,Map<Products, Integer> item) {
        this.stock = stock;
        this.item = item;
    }

    public Map<Products, Integer> getItem() {
        return item;
    }

    public Map<Products, Integer> getFreeItem() {
        return freeItem;
    }

    public void getOneItemFree(Products products) {
        item.put(products, item.get(products) + 1);
        freeItem.put(products, freeItem.getOrDefault(products,0)+1);
        stock.decreasePromotionStock(products);
    }

    public void changeFreeItem(Products products, int quantity){
        freeItem.put(products, quantity);
    }

    public void addDisCountItem(Products products,int quantity){
        freeItem.put(products, freeItem.getOrDefault(products,0) + quantity);
    }

    public void addBuyItem(Map<Products, Integer> additionalItem) {
        item.putAll(additionalItem);
    }

    public void cancelBuy(Products products, int quantity) {
        item.put(products, item.get(products) - quantity);
    }

}
