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
        validateDate(startDate,endDate);
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if(endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_END_DATE.getMessage());
        }
    }

    public boolean isActivePromotion(LocalDate valueDate) {
        return !valueDate.isBefore(startDate) && !valueDate.isAfter(endDate);
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
