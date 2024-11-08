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
    private final String clientInput;
    private final Map<String, Integer> item = new HashMap<>();

    public Map<String, Integer> getItem() {
        return item;
    }

    public Buy(String clientInput) {
        this.clientInput = clientInput;
        initialize(findProducts(),findQuantity());
    }

    private void initialize(List<String> products, List<Integer> quantity){
        IntStream.range(0,products.size())
                .forEach(i->item.put(products.get(i),quantity.get(i)));
    }

    public List<String> findProducts(){
        List<String> productNames = new ArrayList<>();
        Pattern productNamePattern = Pattern.compile("([가-힣]+)");
        Matcher productNamematcher = productNamePattern.matcher(clientInput);

        while (productNamematcher.find()) {
            productNames.add(productNamematcher.group());
        }
        return productNames;
    }

    private List<Integer> findQuantity(){
        List<Integer> quantity = new ArrayList<>();
        Pattern quantityPattern = Pattern.compile("([0-9]+)");
        Matcher quantityMatcher = quantityPattern.matcher(clientInput);

        while (quantityMatcher.find()){
            String number = quantityMatcher.group();
            quantity.add(validateIsNumeric(number));
        }
        return quantity;
    }
    private int validateIsNumeric(String number){
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorMessage.NOT_NUMERIC_ERROR.getMessage());
        }
    }

}
