package store.domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Stock {

    HashMap<Products, Integer> promotionStock = new HashMap<>();
    HashMap<Products, Integer> noPromotionStock = new HashMap<>();

    public HashMap<Products, Integer> getPromotionStock() {
        return promotionStock;
    }

    public void findQuantity(){
        try{BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                String[] split = line.split(",");
                if(split[0].equals("name") || split[2].equals("quantity")) continue;
                if(split[3].equals("null")){
                    noPromotionStock.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
                    continue;
                }
                promotionStock.put(Products.findProduct(split[0]), Integer.parseInt(split[2]));
            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void minusQuantity(String productName, int quantity){
        findQuantity();
        Products product = Products.findProduct(productName);

        if (Products.isPromotionProduct(productName)) {
            if(isZeroQuantity(promotionStock,product)) return;
            promotionStock.put(product, promotionStock.get(product) - 1);
            return;
        }

        if(isZeroQuantity(noPromotionStock,product)) return;
        noPromotionStock.put(product, noPromotionStock.get(product) - 1);
    }

    private boolean isZeroQuantity(HashMap<Products, Integer> stock, Products products){
        return stock.get(products) == 0;
    }

}
