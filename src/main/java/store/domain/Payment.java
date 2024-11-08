package store.domain;

import java.util.stream.IntStream;

public class Payment {
    private int price;

    public int getPrice() {
        return price;
    }

    private void subtract(int price) {
        this.price -= price;
    }

    private void add(int price) {
        this.price += price;
    }

    public void calculateTotalPrice(Products products, int quantity) {
        IntStream.range(0, quantity)
                .forEach(i -> add(products.getPrice()));
    }
}
