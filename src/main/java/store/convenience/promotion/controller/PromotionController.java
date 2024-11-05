package store.convenience.promotion.controller;

import store.convenience.promotion.service.PromotionService;

public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
}
