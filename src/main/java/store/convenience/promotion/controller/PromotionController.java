package store.convenience.promotion.controller;

import java.util.List;
import store.convenience.promotion.service.PromotionService;
import store.global.util.Reader;
import store.global.util.StoreConstant;

public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public void start() {
        List<String> promotionData = Reader.readFiles(StoreConstant.PROMOTION_PATH);
        promotionData.forEach(p -> promotionService.create(p));
    }

}
