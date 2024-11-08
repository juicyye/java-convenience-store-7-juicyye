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

    public int calculateExcessQuantity(int orderCount, LocalDate currentDate) {
        if(checkPromotion(currentDate)) {
            if (orderCount > quantity) {
                return calculateExceeded(orderCount);
            }
        }
        return 0;
    }

    private int calculateExceeded(int orderCount) {
        int promotions = getPromotion().totalPromotions();
        int unitsPerPromotion = quantity / promotions;
        return orderCount - unitsPerPromotion * promotions;
    }

    public int remainingForBonus(int orderCount, LocalDate currentDate) {
        if(checkPromotion(currentDate)) {
            return getPromotion().calculateRemaining(orderCount);
        }
        return 0;
    }

    public int calculateBonusQuantity(int orderCount, LocalDate checkDate) {
        if (checkPromotion(checkDate)) {
            int bonusQuantity = getPromotion().getDetails().bonusQuantity();
            int availableCount = Math.min(orderCount, quantity);
            return availableCount / getPromotion().totalPromotions() * bonusQuantity;
        }
        return 0;
    }

    private boolean checkPromotion(LocalDate currentDate) {
        return getPromotion() != null && promotion.isActivePromotion(currentDate);
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