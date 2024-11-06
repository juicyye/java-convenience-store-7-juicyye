package store.convenience.order.controller.resp;

public record PromotionCheckResult(
        String itemName,
        int count,
        boolean isPromotion,
        boolean hasBonus,
        boolean isExceeded,
        int freeItemCount

) {
}
