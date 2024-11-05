package store.convenience.promotion.domain;

import java.time.LocalDate;
import store.global.util.ErrorMessage;

public class Promotion {

    private PromotionDetails details;
    private LocalDate startDate;
    private LocalDate endDate;

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
        return startDate.isAfter(valueDate) && endDate.isBefore(valueDate);
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
