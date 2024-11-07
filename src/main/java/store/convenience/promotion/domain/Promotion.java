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

    public boolean isActivePromotion(LocalDate valueDate) {
        return !valueDate.isBefore(startDate) && !valueDate.isAfter(endDate);
    }

    public int calculateBonusQuantity(int orderCount, int quantity) {
        int bonusQuantity = details.bonusQuantity();
        int availableCount = Math.min(orderCount, quantity);
        return availableCount / totalPromotions() * bonusQuantity;
    }

    public int isBonusApplicable(int orderCount) {
        int purchaseQuantity = details.purchaseQuantity();
        int bonusQuantity = details.bonusQuantity();
        if (purchaseQuantity == 1) {
            return isSinglePurchaseWithOddOrder(orderCount,bonusQuantity);
        }
        return isEligibleForPromotion(orderCount, purchaseQuantity,bonusQuantity);
    }

    private int isSinglePurchaseWithOddOrder(int orderCount,int bonusQuantity) {
        if (orderCount % 2 == 1) {
            return bonusQuantity;
        }
        return 0;
    }

    private int isEligibleForPromotion(int orderCount, int purchaseQuantity, int bonusQuantity) {
        if ((orderCount - purchaseQuantity) % totalPromotions() == 0) {
            return bonusQuantity;
        }
        return 0;
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


}
