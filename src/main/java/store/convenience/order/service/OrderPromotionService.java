package store.convenience.order.service;

import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public class OrderPromotionService {

    private final ProductRepository productRepository;

    public OrderPromotionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean checkPromotion(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.getApplicablePromotion()
                .map(promotion -> promotion.isActivePromotion(createReqDto.currentDate()))
                .orElse(false);
    }

    public int isEligibleForBonus(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.remainingForBonus(createReqDto.count(), createReqDto.currentDate());
    }

    public int calculateBonusQuantity(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.calculateBonusQuantity(createReqDto.currentDate());
    }

    public int calculateExcessQuantity(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.calculateExcessQuantity(createReqDto.count(), createReqDto.currentDate());
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

}