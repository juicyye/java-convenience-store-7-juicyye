package store.convenience.order.controller;

import static store.global.util.StoreConstant.STANDARD_NUMBER;

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
            try {
                executeOrderFlow();
                printReceipt();
            } catch (RuntimeException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        } while (processRepurchase());
        inputView.close();
    }

    private void executeOrderFlow() {
        showProductInventory();
        List<OrderCreateReqDto> createReqDtos = purchaseOrderItem();
        List<OrderCreateReqDto> updatedCreateReqDtos = new ArrayList<>();
        processPromotions(createReqDtos, updatedCreateReqDtos);
        orderService.process(updatedCreateReqDtos, hasMemberShip());
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

    private void processPromotions(List<OrderCreateReqDto> createReqDtos,
                                   List<OrderCreateReqDto> updatedCreateReqDtos) {
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            if (orderPromotionService.checkPromotion(createReqDto)) {
                createReqDto = handlerPromotions(createReqDto);
            }
            updatedCreateReqDtos.add(createReqDto);
        }
    }

    private OrderCreateReqDto handlerPromotions(OrderCreateReqDto createReqDto) {
        int exceededCount = orderPromotionService.determineExcessQuantity(createReqDto);
        if (exceededCount > STANDARD_NUMBER) {
            return handleExceededPromotion(createReqDto, exceededCount);
        }
        int bonusCount = orderPromotionService.getEligibleBonusItemCount(createReqDto);
        if (bonusCount > STANDARD_NUMBER) {
            return handleBonusPromotion(createReqDto, bonusCount);
        }
        return createReqDto;
    }

    private OrderCreateReqDto handleExceededPromotion(OrderCreateReqDto createReqDto, int exceededCount) {
        Command command = inputProcessor.execute(() -> {
            OutputView.printOverPromotionPurchase(createReqDto.item().getName(), exceededCount);
            return inputView.readCommand();
        });

        if (command.equals(Command.REJECT)) {
            return orderAdjustmentService.excludeExceededQuantity(createReqDto, exceededCount);
        }
        return createReqDto;
    }

    private OrderCreateReqDto handleBonusPromotion(OrderCreateReqDto createReqDto, int bonusCount) {
        Command command = inputProcessor.execute(() -> {
            OutputView.printPromotion(createReqDto.item().getName(), bonusCount);
            return inputView.readCommand();
        });

        if (command.equals(Command.ACCEPT)) {
            return orderAdjustmentService.applyBonus(createReqDto, bonusCount);
        }
        return createReqDto;
    }

    private void printReceipt() {
        Order order = orderService.getLatestOrder();
        String message = orderMessageService.showReceipt(order);
        OutputView.printReceipt(message);
    }

    private boolean hasMemberShip() {
        return inputProcessor.execute(() -> {
            OutputView.printMembership();
            return inputView.readCommand().equals(Command.ACCEPT);
        });
    }

    private boolean processRepurchase() {
        return inputProcessor.execute(() -> {
            OutputView.printRepurchase();
            return inputView.readCommand().equals(Command.ACCEPT);
        });
    }

}