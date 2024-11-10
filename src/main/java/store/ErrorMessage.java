package store;

public enum ErrorMessage {
    NOT_NUMERIC_ERROR("숫자를 입력해주세요"),
    YES_OR_NO_ANSWER_ERROR("Y/N으로 입력해주세요."),
    EXCEED_QUANTITY_ERROR("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    ;

    public static final String HEADER = "[ERROR]";
    private final String message;

    ErrorMessage(String bodyMessage) {
        this.message = String.format("%s %s", HEADER, bodyMessage);
    }

    public String getMessage() {
        return message;
    }
}
