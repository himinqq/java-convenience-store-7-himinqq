package store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.ErrorMessage;
import store.domain.Buy;
import store.domain.MemberShipDiscountPolicy;
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

    public void start() {
        outputView.printWelcome();
        Stock stock = new Stock();
        outputView.printStock(stock);
        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();
        Payment payment = new Payment(promotionDiscountPolicy);

        Buy buy = processPurchaseItems(stock, payment);

        boolean wantToExtraPurchase;

        do{
            for (Entry<Products, Integer> entry : buy.getItem().entrySet()) {
                Products product = entry.getKey();
                Integer quantity = entry.getValue();

                checkNeedAdditionalProductForPromotion(promotionDiscountPolicy, product, quantity, buy, payment);

                if (promotionDiscountPolicy.checkPromotionCondition(product, quantity)) {
                    Integer currentStockList = stock.getPromotionStock().get(product);

                    int availableCount = promotionDiscountPolicy.calculateDiscountCount(product, currentStockList);
                    int expectedCount = promotionDiscountPolicy.calculateDiscountCount(product, quantity);
                    int availableQuantity = promotionDiscountPolicy.calculateDiscountQuantity(product, currentStockList);
                    int requireOriginalPriceQuantity = quantity - availableQuantity;

                    if (expectedCount > availableCount) {
                        boolean answer = inputView.requestApplyOutOfStockNoPromotion(product.getName(), requireOriginalPriceQuantity);
                        if (answer) { // 정가 결제할게요
                            processBuyNGetOneFreeDiscount(payment, product, currentStockList, buy, promotionDiscountPolicy);
                        }
                        // 취소해주세요
                    }

                    processBuyNGetOneFreeDiscount(payment, product, quantity, buy, promotionDiscountPolicy);
                }
            }
            processApplyMembershipDiscount(payment, buy);
            wantToExtraPurchase = inputView.requestExtraPurchase();
            if(wantToExtraPurchase){
                Map<Products, Integer> additionalItems = processPurchaseItems(stock, payment).getItem();
                buy.addBuyItem(additionalItems);
            }
        }while (wantToExtraPurchase);
        outputView.printReceipt(buy.getItem(), buy.getFreeItem(), payment.integratePriceForReceipt());
    }

    private void processApplyMembershipDiscount(Payment payment, Buy buy) {
        boolean applyMembershipDiscount = inputView.requestApplyMembershipDiscount();
        if (applyMembershipDiscount) {
            payment.applyMembershipDiscount(buy, new MemberShipDiscountPolicy());
        }
    }

    private Buy processPurchaseItems(Stock stock, Payment payment) {
        HashMap<Products, Integer> purchaseItem = validatePurchaseItem(stock);
        Buy buy = new Buy(stock, purchaseItem);
        buy.getItem().forEach(payment::incrementPriceForQuantity);
        return buy;
    }

    private void processBuyNGetOneFreeDiscount(Payment payment, Products product, Integer quantity, Buy buy,
                                               PromotionDiscountPolicy promotionDiscountPolicy) {
        payment.applyBuyNGetOneFreeDiscount(product, quantity);
        buy.addDisCountItem(product,
                promotionDiscountPolicy.calculateDiscountCount(product, quantity));
    }

    private void checkNeedAdditionalProductForPromotion(PromotionDiscountPolicy promotionDiscountPolicy,
                                                        Products product, Integer quantity, Buy buy,
                                                        Payment payment) {
        if (promotionDiscountPolicy.checkPromotionCondition(product, quantity) &&
                promotionDiscountPolicy.needExtraQuantityForPromotion(product, quantity)) {
            processAdditionalProductForPromotion(product, buy, payment);
        }
    }

    private void processAdditionalProductForPromotion(Products product, Buy buy, Payment payment) {
        if (inputView.requestExtraQuantityForApplyPromotion(product.getName())) {
            buy.getOneItemFree(product);
            payment.incrementPromotionPriceForQuantity(product, 1);
        }
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
        Integer promotionStock = stock.getPromotionStock().get(products);
        Integer noPromotionStock = stock.getNoPromotionStock().get(products);

        if (Products.isPromotionProduct(products.getName()) && stock.getPromotionStock().get(products) < quantity) {
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