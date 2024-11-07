package store.convenience.promotion.domain;

public record PromotionDetails(
        String name,
        int purchaseQuantity,
        int bonusQuantity
) {}
