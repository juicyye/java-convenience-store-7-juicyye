package store.convenience.order.domain;

import java.util.ArrayList;
import java.util.List;
import store.convenience.product.domain.Item;

public class Discount {

    private List<ItemCount> itemCounts = new ArrayList<>();
    private int totalPrice;
    private int promotionDiscount;
    private int membershipDiscount;

    public Discount(int totalPrice, int promotionDiscount, int membershipDiscount) {
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public void addItemCount(ItemCount itemCount) {
        itemCounts.add(itemCount);
    }
}
