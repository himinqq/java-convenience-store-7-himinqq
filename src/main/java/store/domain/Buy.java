package store.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import store.ErrorMessage;

public class Buy {
    private final Map<Products, Integer> item = new HashMap<>();
    private final Map<Products, Integer> freeItem = new HashMap<>();
    private final Stock stock;

    public Buy(Stock stock) {
        this.stock = stock;
    }

    public Map<Products, Integer> getItem() {
        return item;
    }

    public Map<Products, Integer> getFreeItem() {
        return freeItem;
    }

    public void buyItemByClientInput(List<Products> products, List<Integer> quantity) {
        addBuyItem(products,quantity);
    }
    public void getOneItemFree(Products products) {
        freeItem.put(products, freeItem.get(products)+1);
        stock.decreasePromotionStock(products);
    }

    private void addBuyItem(List<Products> products, List<Integer> quantity) {
        IntStream.range(0, products.size())
                .forEach(i -> item.put(products.get(i),
                        item.getOrDefault(item.get(products.get(i)), 0) + quantity.get(i)));
    }

    public void cancelBuy(Products products, int quantity) {
        item.put(products, item.get(products) - quantity);
    }


}
