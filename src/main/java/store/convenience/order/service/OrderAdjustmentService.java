package store.convenience.order.service;

import store.convenience.order.controller.req.OrderCreateReqDto;

public class OrderAdjustmentService {

    public OrderCreateReqDto applyBonus(OrderCreateReqDto createReqDto, int bonusCount) {
        return applyQuantityAdjustment(createReqDto, bonusCount);
    }


    private OrderCreateReqDto excludeExceededQuantity(OrderCreateReqDto createReqDto, int exceededQuantity) {
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
