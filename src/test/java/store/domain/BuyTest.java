package store.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class BuyTest {

    static Stream<Arguments> purchase() {
        return Stream.of(
                Arguments.of("사이다", 2),
                Arguments.of("감자칩", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("purchase")
    void 사용자가입력한_구매상품과수량을_구분하여_저장한다(String product, int quantity) {
        Buy buy = new Buy("[사이다-2],[감자칩-1]");
        Integer testQuantity = buy.getItem().get(product);
        assertThat(testQuantity).isEqualTo(quantity);
    }
}