package store.convenience.promotion.domain;

import java.time.LocalDate;
import store.global.util.ErrorMessage;

public class Promotion {

    private final PromotionDetails details;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(PromotionDetails details, LocalDate startDate, LocalDate endDate) {
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
        validateDate(startDate, endDate);
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_END_DATE.getMessage());
        }
    }

    public boolean isActivePromotion(LocalDate checkDate) {
        return !checkDate.isBefore(startDate) && !checkDate.isAfter(endDate);
    }

    public int calculateRemaining(int orderCount) {
        int nearestMultiple = getNearestMultiple(orderCount);
        int bonusQuantity = getDetails().bonusQuantity();
        int difference = nearestMultiple - orderCount;

        if (difference > 0 && difference <= bonusQuantity) {
            return difference;
        }
        return 0;
    }

    private int getNearestMultiple(int orderCount) {
        int total = totalPromotions();
        return ((orderCount + total-1) / total) * total;
    }

    public int totalPromotions() {
        return details.purchaseQuantity() + details.bonusQuantity();
    }

    public PromotionDetails getDetails() {
        return details;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int calculateBonus(int orderCount, Integer quantity) {
        int availableCount = Math.min(orderCount, quantity);
        return (availableCount / totalPromotions()) * details.bonusQuantity();
    }

}