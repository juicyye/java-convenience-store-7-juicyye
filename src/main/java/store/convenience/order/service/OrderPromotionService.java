package store.convenience.order.service;

import java.time.LocalDate;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public class OrderPromotionService {

    private final ProductRepository productRepository;

    public OrderPromotionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean checkPromotion(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return isPromotionActive(product.getPromotion(), createReqDto.currentDate());
    }

    private boolean isPromotionActive(Promotion promotion, LocalDate currentDate) {
        return promotion != null && promotion.isActivePromotion(currentDate);
    }

    public int calculateBonusQuantity(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.getPromotion().calculateBonusQuantity(createReqDto.count(), product.getQuantity());
    }

    public int calculateExcessQuantity(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        if (createReqDto.count() > product.getQuantity()) {
            return calculateExceeded(createReqDto.count(), product);
        }
        return 0;
    }

    private int calculateExceeded(int orderCount, Product product) {
        int promotions = product.getPromotion().totalPromotions();
        int unitsPerPromotion = product.getQuantity() / promotions;
        return orderCount - unitsPerPromotion * promotions;
    }

    public int calculateOrderAmounts(OrderCreateReqDto createReqDto) {
        Product product = getProduct(createReqDto.itemName());
        return product.getItem().getPrice() * createReqDto.count();
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }


}