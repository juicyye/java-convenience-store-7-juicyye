package store.convenience.promotion.service;

import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.service.port.PromotionRepository;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;
import store.global.util.Parser;

public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionRequestMapper mapper = PromotionRequestMapper.getInstance();

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void create(String promotionInput) {
        String[] promotionParts = Parser.splitByComma(promotionInput);
        promotionRepository.save(mapper.create(promotionParts));
    }

    public Promotion getPromotion(String name) {
        return promotionRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PROMOTION.getMessage()));
    }

}
