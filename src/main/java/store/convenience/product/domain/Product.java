package store.convenience.product.domain;

import java.time.LocalDate;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.NotEnoughStockException;
import store.global.util.ErrorMessage;

public class Product {
    private Item item;
    private Integer quantity;
    private Promotion promotion;

    public Product(Item item, Integer quantity, Promotion promotion) {
        this.item = item;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void removeStock(int count) {
        int restStock = quantity - count;
        if (restStock < 0) {
            throw new NotEnoughStockException(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
        this.quantity -= count;
    }

    public boolean isActivatePromotion(LocalDate currentDate) {
        return promotion.isActivePromotion(currentDate);
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
