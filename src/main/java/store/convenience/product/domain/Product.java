package store.convenience.product.domain;

import java.time.LocalDate;
import java.util.Optional;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.NotEnoughStockException;
import store.global.exception.ErrorMessage;

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
        if (canApplyPromotion(currentDate)) {
            if (orderCount > quantity) {
                return calculateExceeded(orderCount);
            }
        }
        return 0;
    }

    private int calculateExceeded(int orderCount) {
        int promotions = promotion.totalPromotions();
        int unitsPerPromotion = quantity / promotions;
        return orderCount - unitsPerPromotion * promotions;
    }

    public int checkFreeItemCount(int orderCount, LocalDate currentDate) {
        if (canApplyPromotion(currentDate)) {
            return promotion.checkFreeItemAvailability(orderCount);
        }
        return 0;
    }

    public int calculateBonusQuantity(int orderCount, LocalDate localDate) {
        if (canApplyPromotion(localDate)) {
            int availableCount = Math.min(orderCount, quantity);
            return promotion.calculateBonus(availableCount);
        }
        return 0;
    }

    public Optional<Promotion> getApplicablePromotion() {
        return Optional.ofNullable(promotion);
    }

    public boolean canApplyPromotion(LocalDate checkDate) {
        return promotion != null && promotion.isActivePromotion(checkDate);
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

}