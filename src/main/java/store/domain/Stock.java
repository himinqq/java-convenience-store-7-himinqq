package store.domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


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

    public HashMap<Products, Integer> initializePromotionStock(){
        HashMap<Products,Integer> promotionInventory = new HashMap<>();
        try{BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] split = line.split(",");
                if(split[0].equals("name") || split[2].equals("quantity")) continue;
                if(!split[3].equals("null")){
                    promotionInventory.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
                }
            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return promotionInventory;
    }

    public HashMap<Products, Integer> initializeNoPromotionStock(){
        HashMap<Products, Integer> noPromotionInventory = new HashMap<>();
        try{BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] split = line.split(",");
                if(split[0].equals("name") || split[2].equals("quantity")) continue;
                if(split[3].equals("null")){
                    noPromotionInventory.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
                }
            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noPromotionInventory;
    }

    public void minusQuantity(Products products, int quantity){
        if(Products.isPromotionProduct(products.getName())){
            decreasePromotionStock(products,quantity);
            return;
        }
        decreaseNoPromotionStock(products,quantity);
    }
    public void decreasePromotionStock(Products product, int quantity){
        if(isNotZeroQuantity(promotionStock,product)){
            promotionStock.put(product, promotionStock.get(product) - quantity);
        }
    }
    public void decreaseNoPromotionStock(Products product, int quantity){
        if(isNotZeroQuantity(noPromotionStock,product)){
            noPromotionStock.put(product, noPromotionStock.get(product) - quantity);
        }
    }

    private boolean isNotZeroQuantity(HashMap<Products, Integer> stock, Products products){
        return stock.get(products) != 0 || stock.get(products) != null;
    }
}
