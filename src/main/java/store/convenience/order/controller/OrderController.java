package store.convenience.order.controller;

import java.util.List;
import store.convenience.order.controller.input.InputView;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.PromotionCheck;
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

    public void start(){
        List<OrderCreateReqDto> createReqDtos = inputView.readItems();
        List<PromotionCheck> promotionChecks = checkService.checkPromotion(createReqDtos);
        if(!promotionChecks.isEmpty()){
            handlerPromotions(promotionChecks);
        }

        boolean memberShip = hasMemberShip();
        Discount discount = orderPromotionService.calculateOrderDiscount(promotionChecks, memberShip);

        orderService.order(promotionChecks, discount);

    }

    private boolean hasMemberShip() {
        OutputView.printMembership();
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            return true;
        }
        return false;
    }

    private void handlerPromotions(List<PromotionCheck> checkResults) {
        for (PromotionCheck promotionCheck : checkResults) {
            String itemName = promotionCheck.getProduct().getItem().getName();
            int promotionCount = promotionCheck.getBonusItemCount();
            if (promotionCheck.isBonusAvailable()) {
                handleBonusPromotion(promotionCheck, itemName, promotionCount);
            }
            if (promotionCheck.isExceeded()) {
                handleExceededPromotion(promotionCheck, itemName, promotionCount);
            }
        }
    }

    private void handleBonusPromotion(PromotionCheck promotionCheck, String itemName, int promotionCount) {
        OutputView.printPromotion(itemName, promotionCount);
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            orderPromotionService.bonus(promotionCheck);
        }
    }

    private void handleExceededPromotion(PromotionCheck promotionCheck, String itemName, int promotionCount) {
        OutputView.printOverPromotionPurchase(itemName, promotionCount);
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            orderPromotionService.exceed(promotionCheck);
        }
    }

}
