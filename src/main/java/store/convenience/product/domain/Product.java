package store.convenience.product.domain;

import java.time.LocalDate;
import java.util.Optional;
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
        int promotions = promotion.totalPromotions();
        int unitsPerPromotion = quantity / promotions;
        return orderCount - unitsPerPromotion * promotions;
    }

    public int remainingForBonus(int orderCount, LocalDate currentDate) {
        if(checkPromotion(currentDate)) {
            return promotion.calculateRemaining(orderCount);
        }
        return 0;
    }

    public int calculateBonusQuantity(LocalDate checkDate) {
        if (checkPromotion(checkDate)) {
            return promotion.getDetails().bonusQuantity();
        }
        return 0;
    }

    private boolean checkPromotion(LocalDate currentDate) {
        return promotion != null && promotion.isActivePromotion(currentDate);
    }

    public Optional<Promotion> getApplicablePromotion(){
        return Optional.ofNullable(promotion);
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

}