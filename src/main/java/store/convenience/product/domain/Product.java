package store.convenience.product.domain;

import store.convenience.promotion.domain.Promotion;

public class Product {
    private Item item;
    private Integer quantity;
    private Promotion promotion;

    public Product(Item item, Integer quantity, Promotion promotion) {
        this.item = item;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
