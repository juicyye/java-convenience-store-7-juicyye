package store.convenience.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.PromotionCheck;
import store.convenience.order.service.port.DateTimeHolder;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.PromotionDetails;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public class CheckService {

    private final ProductRepository productRepository;
    private final DateTimeHolder dateTimeHolder;

    public CheckService(ProductRepository productRepository,
                        DateTimeHolder dateTimeHolder) {
        this.productRepository = productRepository;
        this.dateTimeHolder = dateTimeHolder;
    }

    public List<PromotionCheck> checkPromotion(List<OrderCreateReqDto> createReqDtos) {
        List<PromotionCheck> results = new ArrayList<>();

        for (OrderCreateReqDto createReqDto : createReqDtos) {
            Product product = productRepository.findByNameAndPromotion(createReqDto.itemName())
                    .orElseThrow(() -> new NotFoundException(
                            ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
            checkProductPromotion(createReqDto, product, results);
        }
        return results;
    }

    private void checkProductPromotion(OrderCreateReqDto createReqDto, Product product,
                                       List<PromotionCheck> results) {
        PromotionCheck result = checkAndCalculatePromotion(product, createReqDto);
        results.add(result);
    }

    private PromotionCheck checkAndCalculatePromotion(Product product, OrderCreateReqDto createReqDto) {
        if (!isValidPromotion(product, dateTimeHolder.now().toLocalDate())) {
            return createCheckResult(product, createReqDto.count(), false, false, false, 0, 0);
        }

        PromotionDetails details = product.getPromotion().getDetails();
        int count = createReqDto.count();
        int availablePromotions = calculateAvailablePromotions(product.getQuantity(), count, details);
        int bonusCount = count / details.purchaseQuantity() * details.bonusQuantity();

        return calculatePromotionResult(product, count, details, bonusCount, availablePromotions);
    }

    private boolean isValidPromotion(Product product, LocalDate now) {
        return product.getPromotion() != null && product.getPromotion().isActivePromotion(now);
    }

    private int calculateAvailablePromotions(int quantity, int count, PromotionDetails details) {
        int maxPromotions = quantity / details.totalPromotions();
        return count - details.totalPromotions() * maxPromotions;
    }

    private PromotionCheck calculatePromotionResult(Product product, int count, PromotionDetails details,
                                                    int bonusCount, int availablePromotions) {
        if (count == details.purchaseQuantity()) {
            return createCheckResult(product, count, true, true, false, details.bonusQuantity(), 0);
        }

        if (availablePromotions > 0) {
            return createCheckResult(product, count, true, false, true, bonusCount, availablePromotions);
        }
        return createCheckResult(product, count, true, false, false, 0, 0);
    }

    private PromotionCheck createCheckResult(Product product, int count, boolean isPromotion,
                                             boolean hasBonus, boolean isExceeded, int bonus, int exceedCount) {
        return new PromotionCheck(
                product, count, isPromotion,
                hasBonus, isExceeded, bonus, exceedCount);
    }

}
