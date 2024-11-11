package store.convenience.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Discount {

    private List<ItemCount> itemCounts = new ArrayList<>();
    private int promotionDiscount;
    private int membershipDiscount;

    public Discount(List<ItemCount> itemCounts, int promotionDiscount, int membershipDiscount) {
        this.itemCounts = itemCounts;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int getTotalDiscount() {
        return promotionDiscount + membershipDiscount;
    }

    public List<ItemCount> getItemCounts() {
        return Collections.unmodifiableList(itemCounts);
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

}