package store.convenience.order.domain;

import store.convenience.product.domain.Item;

public class ItemCount {
    private Item item;
    private int count;

    public ItemCount(Item item, int count) {
        this.item = item;
        this.count = count;
    }
}
