package store.convenience.order.controller;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.input.InputProcessor;
import store.convenience.order.controller.input.InputView;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Order;
import store.convenience.order.service.OrderAdjustmentService;
import store.convenience.order.service.OrderMessageService;
import store.convenience.order.service.OrderPromotionService;
import store.convenience.order.service.OrderService;
import store.convenience.product.service.ProductMessageService;
import store.global.util.OutputView;

public class OrderController {

    private final OrderService orderService;
    private final InputView inputView;
    private final OrderPromotionService orderPromotionService;
    private final OrderAdjustmentService orderAdjustmentService;
    private final InputProcessor inputProcessor;
    private final ProductMessageService productMessageService;
    private final OrderMessageService orderMessageService;

    public OrderController(OrderService orderService, InputView inputView, OrderPromotionService orderPromotionService,
                           OrderAdjustmentService orderAdjustmentService, InputProcessor inputProcessor,
                           ProductMessageService productMessageService, OrderMessageService orderMessageService) {
        this.orderService = orderService;
        this.inputView = inputView;
        this.orderPromotionService = orderPromotionService;
        this.orderAdjustmentService = orderAdjustmentService;
        this.inputProcessor = inputProcessor;
        this.productMessageService = productMessageService;
        this.orderMessageService = orderMessageService;
    }

    public void start() {
        do {
            inputProcessor.execute(() -> {
                showProductInventory();
                List<OrderCreateReqDto> createReqDtos = purchaseOrderItem();
                List<OrderCreateReqDto> updatedCreateReqDtos = new ArrayList<>();
                processPromotions(createReqDtos, updatedCreateReqDtos);

                orderService.process(updatedCreateReqDtos, hasMemberShip());
                List<Order> orders = orderService.getAllOrders();
                printReceipt(orders);
                return null;
            });

        } while (processRepurchase());
    }

    private void showProductInventory() {
        OutputView.printGreeting();
        String message = productMessageService.showProductInventory();
        OutputView.printInventoryHeader();
        OutputView.printInventory(message);
    }

    private List<OrderCreateReqDto> purchaseOrderItem() {
        return inputProcessor.execute(inputView::readItems);
    }

    private void processPromotions(List<OrderCreateReqDto> createReqDtos, List<OrderCreateReqDto> updatedCreateReqDtos) {
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            if (orderPromotionService.checkPromotion(createReqDto)) {
                createReqDto = handlerPromotions(createReqDto);
            }
            updatedCreateReqDtos.add(createReqDto);
        }
    }

    private OrderCreateReqDto handlerPromotions(OrderCreateReqDto createReqDto) {
        int exceededCount = orderPromotionService.calculateExcessQuantity(createReqDto);
        if (exceededCount > 0) {
            return handleExceededPromotion(createReqDto, exceededCount);
        }
        int bonusCount = orderPromotionService.isEligibleForBonus(createReqDto);
        if (bonusCount > 0) {
            return handleBonusPromotion(createReqDto,bonusCount);
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

    private OrderCreateReqDto handleBonusPromotion(OrderCreateReqDto createReqDto, int bonusCount) {
        OutputView.printPromotion(createReqDto.itemName(), bonusCount);
        Command command = inputView.readCommand();
        if (command.equals(Command.ACCEPT)) {
            return orderAdjustmentService.applyBonus(createReqDto, bonusCount);
        }
        return createReqDto;
    }

    private void printReceipt(List<Order> orders) {
        String message = orderMessageService.showReceipt(orders);
        OutputView.printReceipt(message);
    }

    private boolean hasMemberShip() {
        OutputView.printMembership();
        return inputView.readCommand().equals(Command.ACCEPT);
    }

    private boolean processRepurchase() {
        OutputView.printRepurchase();
        return inputView.readCommand().equals(Command.ACCEPT);
    }

}