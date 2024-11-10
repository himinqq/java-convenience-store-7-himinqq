package store.controller;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.ErrorMessage;
import store.domain.Buy;
import store.domain.Payment;
import store.domain.Products;
import store.domain.PromotionDiscountPolicy;
import store.domain.Stock;
import store.service.ConvenienceService;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceService convenienceService;

    public ConvenienceController(InputView inputView, OutputView outputView, ConvenienceService convenienceService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceService = convenienceService;
    }

//반복로직
//    public void start() {
//        outputView.printWelcome();
//        Stock stock = new Stock();
//        outputView.printStock(stock);
//        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();
//        Payment payment = new Payment(promotionDiscountPolicy);
//
//        boolean answer = true;
//        HashMap<Products, Integer> purchaseItem = validatePurchaseItem(stock);
//        Buy buy = new Buy(stock, purchaseItem);
//        answer = inputView.requestExtraPurchase();
//        while(answer){
//            HashMap<Products, Integer> purchase = validatePurchaseItem(stock);
//            buy.addBuyItem(purchase);
//            buy.getItem().forEach(payment::incrementPriceForQuantity);
//            outputView.printReceipt(buy.getItem(),buy.getFreeItem(),payment.integratePriceForReceipt());
//            answer = inputView.requestExtraPurchase();
//        }
//    }

    public void start() {
        outputView.printWelcome();
        Stock stock = new Stock();
        outputView.printStock(stock);
        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();
        Payment payment = new Payment(promotionDiscountPolicy);

        HashMap<Products, Integer> purchaseItem = validatePurchaseItem(stock);
        Buy buy = new Buy(stock, purchaseItem);
        buy.getItem().forEach(payment::incrementPriceForQuantity);

        for (Entry<Products, Integer> entry : buy.getItem().entrySet()) {
            Products product = entry.getKey();
            Integer quantity = entry.getValue();
            if (promotionDiscountPolicy.checkPromotionCondition(product, quantity)) {
                payment.applyBuyNGetOneFreeDiscount(product, quantity);
                if (promotionDiscountPolicy.needExtraQuantityForPromotion(product, quantity)) {
                    if (inputView.requestExtraQuantityForApplyPromotion(product.getName())) {
                        buy.getOneItemFree(product);
                    }
                }
            }

            if (Products.isPromotionProduct(product.getName())) {
                System.out.println();
                int remainQuantityByOutOfStock = stock.decreasePromotionStockAndReturnRemaining(product, quantity);
                if (remainQuantityByOutOfStock != 0) {
                    boolean answer = inputView.requestApplyOutOfStockNoPromotion(Console.readLine(), quantity);
                    if (answer) {
                        stock.decreaseNoPromotionStock(product);
                        payment.incrementPriceForQuantity(product, remainQuantityByOutOfStock);
                        continue;
                    }
                    payment.cancelPayment(product, remainQuantityByOutOfStock);
                    buy.cancelBuy(product, remainQuantityByOutOfStock);
                }
                continue;
            }
            stock.decreaseNoPromotionStock(product);
        }

        outputView.printReceipt(buy.getItem(), buy.getFreeItem(), payment.integratePriceForReceipt());
    }

    public List<Products> findProducts(String clientInput) {
        List<Products> products = new ArrayList<>();
        Pattern productNamePattern = Pattern.compile("([가-힣]+)");
        Matcher productNamematcher = productNamePattern.matcher(clientInput);

        while (productNamematcher.find()) {
            String product = productNamematcher.group();
            products.add(Products.findProduct(product));
        }
        return products;
    }

    private List<Integer> findQuantity(String clientInput) {
        List<Integer> quantity = new ArrayList<>();
        Pattern quantityPattern = Pattern.compile("([0-9]+)");
        Matcher quantityMatcher = quantityPattern.matcher(clientInput);

        while (quantityMatcher.find()) {
            String input = quantityMatcher.group();
            int number = validateIsNumeric(input);
            quantity.add(number);
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

    public HashMap<Products, Integer> validatePurchaseItem(Stock stock) {
        List<Integer> quantity = new ArrayList<>();
        List<Products> products = new ArrayList<>();
        final HashMap<Products, Integer> item = new HashMap<>();
        boolean valid = false;
        while (!valid) {
            try {
                String input = inputView.requestPurchaseStock();
                quantity = findQuantity(input);
                products = findProducts(input);
                for (int i = 0; i < products.size(); i++) {
                    validateExceedQuantity(products.get(i), quantity.get(i), stock);
                }
                valid = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + System.lineSeparator());
            }
        }
        for (int i = 0; i < quantity.size(); i++) {
            item.put(products.get(i), quantity.get(i));
        }
        return item;
    }

    private void validateExceedQuantity(Products products, int quantity, Stock stock) {
        if (Products.isPromotionProduct(products.getName())) {
            stock.checkExceedQuantity(stock.getPromotionStock(), products, quantity);
            return;
        }
        stock.checkExceedQuantity(stock.getNoPromotionStock(), products, quantity);
    }

    //계산 로직
//    for (Entry<Products, Integer> entry : buy.getItem().entrySet()) {
//        Products product = entry.getKey();
//        Integer quantity = entry.getValue();
//        if(promotionDiscountPolicy.checkPromotionCondition(product,quantity)){
//            payment.applyBuyNGetOneFreeDiscount(product,quantity);
//            if(promotionDiscountPolicy.needExtraQuantityForPromotion(product, quantity)){
//                if(inputView.requestExtraQuantityForApplyPromotion(product.getName())){
//                    buy.getOneItemFree(product);
//                }
//            }
//        }
//
//        if(Products.isPromotionProduct(product.getName())){
//            int remainQuantityByOutOfStock = stock.decreasePromotionStockAndReturnRemaining(product, quantity);
//            if(remainQuantityByOutOfStock != 0){
//                boolean answer = inputView.requestApplyOutOfStockNoPromotion(Console.readLine(), quantity);
//                if(answer) {
//                    stock.decreaseNoPromotionStock(product);
//                    payment.incrementPriceForQuantity(product,remainQuantityByOutOfStock);
//                    continue;
//                }
//                payment.cancelPayment(product,remainQuantityByOutOfStock);
//                buy.cancelBuy(product,remainQuantityByOutOfStock);
//            }
//            continue;
//        }
//        stock.decreaseNoPromotionStock(product);
//    }

}
