package store.convenience.order.controller;

import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.service.OrderService;

public class OrderController {

    private final OrderService orderService;
    private final InputView inputView;

    public OrderController(OrderService orderService, InputView inputView) {
        this.orderService = orderService;
        this.inputView = inputView;
    }

    public void start(){
        List<OrderCreateReqDto> createReqDtos = inputView.readItems();
        orderService.processOrder(createReqDtos);

    }
}
