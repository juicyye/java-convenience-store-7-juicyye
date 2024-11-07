package store.convenience.order.controller;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.input.InputView;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.service.CheckService;
import store.convenience.order.service.OrderPromotionService;
import store.convenience.order.service.OrderService;
import store.global.util.OutputView;

public class OrderController {

    private final OrderService orderService;
    private final InputView inputView;
    private final CheckService checkService;
    private final OrderPromotionService orderPromotionService;

    public OrderController(OrderService orderService, InputView inputView, CheckService checkService,
                           OrderPromotionService orderPromotionService) {
        this.orderService = orderService;
        this.inputView = inputView;
        this.checkService = checkService;
        this.orderPromotionService = orderPromotionService;
    }

    public void start() {
        List<OrderCreateReqDto> createReqDtos = inputView.readItems();
        List<OrderCreateReqDto> updatedCreateReqDtos = new ArrayList<>();
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            if (checkService.checkPromotion(createReqDto)) {
                createReqDto = handlerPromotions(createReqDto);
            }
            updatedCreateReqDtos.add(createReqDto);
        }

        boolean memberShip = hasMemberShip();
        orderService.order(updatedCreateReqDtos, memberShip);
    }

    private OrderCreateReqDto handlerPromotions(OrderCreateReqDto createReqDto) {
        int exceededCount = checkService.calculateExcessQuantity(createReqDto);
        if (exceededCount > 0) {
            return handleExceededPromotion(createReqDto, exceededCount);
        }

        int bonusCount = checkService.calculateBonusQuantity(createReqDto);
        if (bonusCount > 0) {
            return handleBonusPromotion(createReqDto, bonusCount);
        }
        return createReqDto;
    }


    private OrderCreateReqDto handleBonusPromotion(OrderCreateReqDto createReqDto, int bonusCount) {
        OutputView.printPromotion(createReqDto.itemName(), bonusCount);
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            return orderPromotionService.applyBonus(createReqDto, bonusCount);
        }
        return createReqDto;
    }

    private OrderCreateReqDto handleExceededPromotion(OrderCreateReqDto createReqDto, int promotionCount) {
        OutputView.printOverPromotionPurchase(createReqDto.itemName(), promotionCount);
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            //todo
            throw new IllegalArgumentException("Exceeded promotion");
        }
        return createReqDto;
    }

    private boolean hasMemberShip() {
        OutputView.printMembership();
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            return true;
        }
        return false;
    }

}
