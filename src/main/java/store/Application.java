package store;

import camp.nextstep.edu.missionutils.Console;
import store.controller.ConvenienceController;
import store.service.ConvenienceService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView(Console::readLine);
        OutputView outputView = new OutputView();
        ConvenienceService convenienceService = new ConvenienceService();
        ConvenienceController convenienceController = new ConvenienceController(inputView, outputView,
                convenienceService);

        convenienceController.start();

    }
}
