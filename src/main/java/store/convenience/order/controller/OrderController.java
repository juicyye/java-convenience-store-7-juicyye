package store.convenience.order.controller;

import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.controller.resp.PromotionCheckResult;
import store.convenience.order.service.CheckService;
import store.convenience.order.service.OrderService;
import store.global.util.OutputView;

public class OrderController {

    private final OrderService orderService;
    private final InputView inputView;
    private final CheckService checkService;

    public OrderController(OrderService orderService, InputView inputView, CheckService checkService) {
        this.orderService = orderService;
        this.inputView = inputView;
        this.checkService = checkService;
    }

    public void start(){
        List<OrderCreateReqDto> createReqDtos = inputView.readItems();
        List<PromotionCheckResult> checkResults = checkService.checkPromotion(createReqDtos);
        if(!checkResults.isEmpty()){
            for (PromotionCheckResult checkResult : checkResults) {
                if (checkResult.bonusAvailable()) {
                    OutputView.printPromotion(checkResult.itemName(), checkResult.freeItemCount());
                    Command command = inputView.readCommand();
                }
                if (checkResult.isExceeded()) {
                    OutputView.printOverPromotionPurchase(checkResult.itemName(), checkResult.freeItemCount());
                    Command command = inputView.readCommand();
                }
            }
        }


    }
}
