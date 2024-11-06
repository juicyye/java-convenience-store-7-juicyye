package store.convenience.order.service;

import static store.global.util.StoreConstant.MAX_MEMBERSHIP_DISCOUNT;
import static store.global.util.StoreConstant.MEMBERSHIP_RATE;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.order.domain.PromotionCheck;
import store.convenience.product.domain.Item;

public class OrderPromotionService {

    public void bonus(PromotionCheck promotionCheck) {
        promotionCheck.addBonus();
    }

    public void exceed(PromotionCheck promotionCheck) {
        promotionCheck.removeCount();
    }

    public Discount calculateOrderDiscount(List<PromotionCheck> promotionChecks, boolean hasMembership) {
        OrderCalculation calculation = calculateOrderAmounts(promotionChecks);
        int membershipDiscount = calculateMembershipDiscount(calculation.totalPrice, hasMembership);

        return new Discount(
                calculation.totalPrice,
                calculation.promotionAmount,
                membershipDiscount
        );
    }

    private OrderCalculation calculateOrderAmounts(List<PromotionCheck> promotionChecks) {
        List<ItemCount> itemCounts = new ArrayList<>();
        int totalPrice = 0;
        int promotionAmount = 0;

        for (PromotionCheck check : promotionChecks) {
            Item item = check.getItem();
            itemCounts.add(new ItemCount(item, check.getBonusItemCount()));

            int itemPrice = item.getPrice();
            promotionAmount += itemPrice * check.getBonusItemCount();
            totalPrice += itemPrice * check.getCount();
        }

        return new OrderCalculation(itemCounts, totalPrice, promotionAmount);
    }

    private int calculateMembershipDiscount(int totalPrice, boolean hasMembership) {
        if (!hasMembership) return 0;
        return (int) Math.min(MAX_MEMBERSHIP_DISCOUNT, totalPrice * MEMBERSHIP_RATE);
    }


    private record OrderCalculation(
            List<ItemCount> itemCounts,
            int totalPrice,
            int promotionAmount
    ) {

    }

}
