package store.convenience.order.controller.resp;

public record PromotionCheckResult(
        String itemName,
        int count,
        boolean bonusAvailable,
        boolean isExceeded,
        int freeItemCount

) {
}
