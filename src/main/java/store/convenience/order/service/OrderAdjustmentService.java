package store.convenience.order.service;

import store.convenience.order.controller.req.OrderCreateReqDto;
import store.global.exception.ErrorMessage;
import store.global.exception.NotEnoughStockException;

public class OrderAdjustmentService {

    public OrderCreateReqDto applyBonus(OrderCreateReqDto createReqDto, int bonusCount) {
        return applyQuantityAdjustment(createReqDto, bonusCount);
    }

    public OrderCreateReqDto excludeExceededQuantity(OrderCreateReqDto createReqDto, int exceededQuantity) {
        if (createReqDto.count() == exceededQuantity) {
            throw new NotEnoughStockException(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
        return applyQuantityAdjustment(createReqDto, -exceededQuantity);
    }

    private OrderCreateReqDto applyQuantityAdjustment(OrderCreateReqDto createReqDto, int adjustmentCount) {
        return new OrderCreateReqDto(
                createReqDto.itemName(),
                createReqDto.count() + adjustmentCount,
                createReqDto.currentDate()
        );
    }

}