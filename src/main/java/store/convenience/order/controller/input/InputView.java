package store.convenience.order.controller.input;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.convenience.order.controller.Command;
import store.convenience.order.controller.req.OrderCreateReqDto;

public class InputView {

    private final InputHandler inputHandler;

    public InputView(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public List<OrderCreateReqDto> readItems() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return inputHandler.parseOrderRequests(Console.readLine());
    }

    public Command readCommand() {
        return inputHandler.parseIntention(Console.readLine());
    }

    public void close() {
        Console.close();
    }

}