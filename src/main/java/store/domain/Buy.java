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
    private final Stock stock;

    public Buy(Stock stock) {
        this.stock = stock;
    }

    public Map<Products, Integer> getItem() {
        return item;
    }

    public void buyProductsByClientInput(String clientInput) {
        addBuyItem(findProducts(clientInput), findQuantity(clientInput));
        item.forEach(stock::minusQuantity);
    }

    public void getOneFree(Products products) {
        item.put(products, item.get(products) + 1);
        stock.minusQuantity(products, 1);
    }

    private void addBuyItem(List<Products> products, List<Integer> quantity) {
        IntStream.range(0, products.size())
                .forEach(i -> item.put(products.get(i),
                        item.getOrDefault(item.get(products.get(i)), 0) + quantity.get(i)));
    }

    public List<Products> findProducts(String clientInput) {
        List<Products> products = new ArrayList<>();
        Pattern productNamePattern = Pattern.compile("([가-힣]+)");
        Matcher productNamematcher = productNamePattern.matcher(clientInput);

        while (productNamematcher.find()) {
            products.add(Products.findProduct(productNamematcher.group()));
        }
        return products;
    }

    private List<Integer> findQuantity(String clientInput) {
        List<Integer> quantity = new ArrayList<>();
        Pattern quantityPattern = Pattern.compile("([0-9]+)");
        Matcher quantityMatcher = quantityPattern.matcher(clientInput);

        while (quantityMatcher.find()) {
            String number = quantityMatcher.group();
            quantity.add(validateIsNumeric(number));
        }
        return quantity;
    }

    private int validateIsNumeric(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.NOT_NUMERIC_ERROR.getMessage());
        }
    }

}
