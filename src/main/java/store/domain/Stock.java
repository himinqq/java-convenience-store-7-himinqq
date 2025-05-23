package store.domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import store.ErrorMessage;


public class Stock {

    private HashMap<Products, Integer> promotionStock;
    private HashMap<Products, Integer> noPromotionStock;

    public Stock() {
        this.promotionStock = initializePromotionStock();
        this.noPromotionStock = initializeNoPromotionStock();
    }

    public HashMap<Products, Integer> getPromotionStock() {
        return promotionStock;
    }

    public HashMap<Products, Integer> getNoPromotionStock() {
        return noPromotionStock;
    }

    public HashMap<Products, Integer> initializePromotionStock() {
        HashMap<Products, Integer> promotionInventory = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                if (split[0].equals("name") || split[2].equals("quantity")) {
                    continue;
                }
                if (!split[3].equals("null")) {
                    promotionInventory.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return promotionInventory;
    }

    public HashMap<Products, Integer> initializeNoPromotionStock() {
        HashMap<Products, Integer> noPromotionInventory = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                if (split[0].equals("name") || split[2].equals("quantity")) {
                    continue;
                }
                if (split[3].equals("null")) {
                    noPromotionInventory.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noPromotionInventory;
    }

    public void decreasePromotionStock(Products product) {
        promotionStock.put(product, promotionStock.get(product) - 1);
    }

    private boolean isZeroQuantity(HashMap<Products, Integer> stock, Products products) {
        return stock.get(products) == 0 || stock.get(products) == null;
    }

    public void checkExceedQuantity(HashMap<Products, Integer> stock, Products products, int quantity) {
        if (stock.get(products) < quantity) {
            throw new IllegalArgumentException(ErrorMessage.EXCEED_QUANTITY_ERROR.getMessage());
        }
    }

    public void decreaseStockByQuantity(HashMap<Products, Integer> stock, Products products, int quantity){
        stock.put(products, stock.get(products) - quantity);
    }

    public void decreaseStock(Map<Products, Integer> item){
        item.entrySet().forEach(i->{
            Products products = i.getKey();
            Integer quantity = i.getValue();
            if(Products.isPromotionProduct(products.getName())){
                validateExceedQuantity(products,quantity);
                decreaseStockByQuantity(promotionStock,products,quantity);
                return;
            }
            decreaseStockByQuantity(noPromotionStock,products,quantity);
        });
    }

    public void validateExceedQuantity(Products products, int quantity) {
        Integer promotionStock = getPromotionStock().get(products);
        Integer noPromotionStock = getNoPromotionStock().get(products);

        if (Products.isPromotionProduct(products.getName()) && getPromotionStock().get(products) < quantity) {
            if (promotionStock + noPromotionStock < quantity) {
                throw new IllegalArgumentException(ErrorMessage.EXCEED_QUANTITY_ERROR.getMessage());
            }
        }
        if (!Products.isPromotionProduct(products.getName())) {
            if (noPromotionStock < quantity) {
                throw new IllegalArgumentException(ErrorMessage.EXCEED_QUANTITY_ERROR.getMessage());
            }
        }
    }


}
