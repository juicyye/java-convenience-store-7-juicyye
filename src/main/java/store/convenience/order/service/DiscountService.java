package store.convenience.order.service;

import static store.global.util.StoreConstant.MAX_MEMBERSHIP_DISCOUNT;
import static store.global.util.StoreConstant.MEMBERSHIP_RATE;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.product.domain.Item;

public class DiscountService {

    private final OrderPromotionService orderPromotionService;

    public DiscountService(OrderPromotionService orderPromotionService) {
        this.orderPromotionService = orderPromotionService;
    }

    public Discount calculateOrderDiscount(List<OrderCreateReqDto> createReqDtos, boolean hasMembership, int totalPrice) {
        List<ItemCount> items = new ArrayList<>();
        int promotionAmount = 0;

        for (OrderCreateReqDto createReqDto : createReqDtos) {
            promotionAmount += calculatePromotion(createReqDto, items);
        }

        int membershipDiscount = calculateMembershipDiscount(totalPrice - promotionAmount, hasMembership);
        return new Discount(items, promotionAmount, membershipDiscount);
    }

    private int calculatePromotion(OrderCreateReqDto createReqDto, List<ItemCount> items) {
        int bonusQuantity = orderPromotionService.calculateBonusQuantity(createReqDto);
        if (bonusQuantity > 0) {
            items.add(new ItemCount(Item.of(createReqDto.itemName()), bonusQuantity));
        }
        return bonusQuantity * Item.of(createReqDto.itemName()).getPrice();
    }

    public int calculateMembershipDiscount(int priceAfterPromotion, boolean hasMembership) {
        if (!hasMembership) {
            return 0;
        }
        double membershipDiscount = Math.min(MAX_MEMBERSHIP_DISCOUNT, priceAfterPromotion * MEMBERSHIP_RATE);
        return (int) Math.floor(membershipDiscount / 100) * 100;
    }

}