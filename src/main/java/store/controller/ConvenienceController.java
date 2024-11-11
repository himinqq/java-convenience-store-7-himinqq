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
        Stock stock = new Stock();
        PromotionDiscountPolicy promotionDiscountPolicy = new PromotionDiscountPolicy();

        boolean wantToExtraPurchase = true;

        while (wantToExtraPurchase) {
            outputView.printWelcome();
            outputView.printStock(stock);
            Payment payment = new Payment(promotionDiscountPolicy);
            Buy buy = processPurchaseItems(stock, payment);

            //test
            for (Entry<Products, Integer> entry : buy.getItem().entrySet()) {
                Products product = entry.getKey();
                Integer quantity = entry.getValue();

                checkNeedAdditionalProductForPromotion(promotionDiscountPolicy, product, quantity, buy, payment);
                processApplyPromotionDiscount(promotionDiscountPolicy, product, quantity, stock, payment, buy);
            }
            processApplyMembershipDiscount(payment, buy);
            outputView.printReceipt(buy.getItem(), buy.getFreeItem(), payment.integratePriceForReceipt());
            stock.decreaseStock(buy.getItem());
            //test

            wantToExtraPurchase = inputView.requestExtraPurchase();
        }
    }

    private Buy processPurchaseItems(Stock stock, Payment payment) {
        HashMap<Products, Integer> purchaseItem = validatePurchaseItem(stock);
        Buy buy = new Buy(stock, purchaseItem);
        buy.getItem().forEach(payment::incrementPriceForQuantity);
        return buy;
    }

    private void processApplyPromotionDiscount(PromotionDiscountPolicy promotionDiscountPolicy, Products product,
                                               Integer quantity,
                                               Stock stock, Payment payment, Buy buy) {
        if (promotionDiscountPolicy.checkPromotionCondition(product, quantity)) {
            int currentStock = stock.getPromotionStock().get(product);

            int expectedCount = promotionDiscountPolicy.calculateDiscountCount(product, quantity);
            int availableCount = promotionDiscountPolicy.calculateDiscountCount(product, currentStock);
            int availableQuantity = promotionDiscountPolicy.calculateDiscountQuantity(product, currentStock);
            int requireRegularPriceQuantity = quantity - availableQuantity;

            boolean decideRegularPay = checkRequireRegularPriceDueToOutOfPromotionStock(expectedCount, availableCount,
                    product,
                    requireRegularPriceQuantity, payment,
                    currentStock, buy,
                    promotionDiscountPolicy);
            if (!decideRegularPay) {
                processBuyNGetOneFreeDiscount(payment, product, quantity, buy, promotionDiscountPolicy);
            }
        }
    }

    private boolean checkRequireRegularPriceDueToOutOfPromotionStock(int expectedCount, int availableCount,
                                                                     Products product, int requireRegularPriceQuantity,
                                                                     Payment payment, int currentStock, Buy buy,
                                                                     PromotionDiscountPolicy promotionDiscountPolicy) {
        if (expectedCount > availableCount) {
            boolean answer = inputView.requestApplyOutOfStockNoPromotion(product.getName(),
                    requireRegularPriceQuantity);
            if (answer) { // 정가 결제할게요
                processBuyNGetOneFreeDiscount(payment, product, currentStock, buy, promotionDiscountPolicy);
                return true;
            }
            // 취소해주세요
            buy.cancelBuy(product, requireRegularPriceQuantity);
            payment.cancelPayment(product, requireRegularPriceQuantity);
        }
        return false;
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

    private void processApplyMembershipDiscount(Payment payment, Buy buy) {
        boolean applyMembershipDiscount = inputView.requestApplyMembershipDiscount();
        if (applyMembershipDiscount) {
            payment.applyMembershipDiscount(buy, new MemberShipDiscountPolicy());
        }
    }

    public List<Products> findProducts(String clientInput) {
        List<Products> products = new ArrayList<>();
        Pattern productNamePattern = Pattern.compile("([가-힣]+)");
        Matcher productNamematcher = productNamePattern.matcher(clientInput);

        while (productNamematcher.find()) {
            String product = productNamematcher.group();
            validateExistProduct(product);
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
                    stock.validateExceedQuantity(products.get(i), quantity.get(i));
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

    public void validateExistProduct(String input){
        if(Products.findProduct(input).equals(Products.NOTHING)){
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PRODUCT_ERROR.getMessage());
        }
    }
}