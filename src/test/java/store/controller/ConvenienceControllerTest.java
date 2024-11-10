package store.controller;

import static org.junit.jupiter.api.Assertions.*;

import camp.nextstep.edu.missionutils.Console;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.Buy;
import store.domain.Payment;
import store.domain.PromotionDiscountPolicy;
import store.domain.Stock;
import store.service.ConvenienceService;
import store.view.InputView;
import store.view.OutputView;

class ConvenienceControllerTest {

    //몫 1 나머지 2 5개 구매 : 1개 가격 차감 + 프로모션 적용 안내 메세지 (1개 추가)
    //몫 0 나머지 1 1개 구매 : 프로모션 적용 안내 메세지 (1개 추가)

    static Stream<Arguments> purchase() {
        return Stream.of(
                Arguments.of("사이다", 2),
                Arguments.of("감자칩", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("purchase")
    void 프로모션을_적용하여_계산한다(String product, int quantity){


    }
}