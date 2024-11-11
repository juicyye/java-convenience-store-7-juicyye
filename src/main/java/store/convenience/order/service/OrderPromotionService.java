package store.convenience.order.service;

import java.time.LocalDate;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.service.port.LocalDateTimeHolder;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.global.exception.ErrorMessage;
import store.global.exception.NotFoundException;

public class OrderPromotionService {

    private final ProductRepository productRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    public OrderPromotionService(ProductRepository productRepository, LocalDateTimeHolder localDateTimeHolder) {
        this.productRepository = productRepository;
        this.localDateTimeHolder = localDateTimeHolder;
    }

    public boolean checkPromotion(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.item().getName());
        return product.canApplyPromotion(localDateTimeHolder.now().toLocalDate());
    }

    public int getEligibleBonusItemCount(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.item().getName());
        return product.remainingBonusQuantity(createReqDto.count(), localDateTimeHolder.now().toLocalDate());
    }

    public int determineBonusQuantity(OrderCreateReqDto createReqDto, LocalDate currentDate) {
        Product product = getProduct(createReqDto.item().getName());
        return product.calculateBonusQuantity(createReqDto.count(), currentDate);
    }

    public int determineExcessQuantity(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.item().getName());
        return product.calculateExcessQuantity(createReqDto.count(), localDateTimeHolder.now().toLocalDate());
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

}