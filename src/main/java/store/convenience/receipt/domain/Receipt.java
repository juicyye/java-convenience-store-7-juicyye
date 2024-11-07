package store.convenience.receipt.domain;

import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;

public class Receipt {
    private ItemCount regularOrders;
    private ItemCount promotionOrders;
    private Discount discount;
    private int totalPrice;
}
