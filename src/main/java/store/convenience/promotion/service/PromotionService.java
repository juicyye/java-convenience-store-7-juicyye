package store.convenience.promotion.service;

import store.convenience.promotion.controller.req.PromotionCreateReqDto;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.service.port.PromotionRepository;
import store.global.util.ErrorMessage;
import store.global.exception.NotFoundException;

public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionRequestMapper mapper = PromotionRequestMapper.getInstance();

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void create(PromotionCreateReqDto createReqDto) {
        promotionRepository.save(mapper.create(createReqDto));
    }

    public Promotion getPromotion(String name) {
        return promotionRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PROMOTION.getMessage()));
    }

}
