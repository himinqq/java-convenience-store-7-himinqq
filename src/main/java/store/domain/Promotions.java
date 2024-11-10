package store.domain;

import java.time.LocalDate;

public enum Promotions {
    SPARKLE_TWO_PULUS_ONE("탄산2+1",2,1,LocalDate.of(2024,1,1),LocalDate.of(2024,12,31)),
    MD_RECOMMEND("MD추천상품",1,1,LocalDate.of(2024,1,1),LocalDate.of(2024,12,31)),
    TIME_SALE("반짝할인", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30)),
    NO_PROMOTION(null, 0, 0, null, null),
    ;

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start;
    private final LocalDate end;

    Promotions(String name, int buy, int get, LocalDate start, LocalDate end) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start = start;
        this.end = end;
    }

    public int getBuy() {
        return buy;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }
}