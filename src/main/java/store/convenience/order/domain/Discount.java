package store.convenience.order.domain;

import java.util.ArrayList;
import java.util.List;
import store.convenience.product.domain.Item;

public class Discount {

    private List<ItemCount> promotionItems = new ArrayList<>();
    private int totalPrice;
    private int promotionDiscount;
    private int membershipDiscount;

    public Discount(List<ItemCount> promotionItems, int totalPrice, int promotionDiscount, int membershipDiscount) {
        this.promotionItems = promotionItems;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}
