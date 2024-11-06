package store.convenience.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.PromotionCheck;
import store.convenience.order.service.port.DateTimeHolder;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.PromotionDetails;

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
            List<Product> products = productRepository.findByName(createReqDto.itemName());
            checkProductPromotion(createReqDto, products, results);
        }
        return results;
    }

    private void checkProductPromotion(OrderCreateReqDto createReqDto, List<Product> products,
                                       List<PromotionCheck> results) {
        for (Product product : products) {
            PromotionCheck result = checkSinglePromotion(product, createReqDto);
            if (result != null) {
                results.add(result);
            }
        }
    }

    private PromotionCheck checkSinglePromotion(Product product, OrderCreateReqDto createReqDto) {
        if (!isValidPromotion(product, dateTimeHolder.now().toLocalDate())) {
            return null;
        }

        PromotionDetails details = product.getPromotion().getDetails();
        int count = createReqDto.count();
        int availablePromotions = calculateAvailablePromotions(product.getQuantity(), count, details);
        int bonusCount = count / details.purchaseQuantity() * details.bonusQuantity();

        return calculatePromotionResult(product.getItem(), count, details, bonusCount, availablePromotions);
    }

    private boolean isValidPromotion(Product product, LocalDate now) {
        return product.getPromotion() != null && product.getPromotion().isActivePromotion(now);
    }

    private int calculateAvailablePromotions(int quantity, int count, PromotionDetails details) {
        int maxPromotions = quantity / details.totalPromotions();
        return count - details.totalPromotions() * maxPromotions;
    }

    private PromotionCheck calculatePromotionResult(Item item, int count, PromotionDetails details,
                                                    int bonusCount, int availablePromotions) {
        if (count == details.purchaseQuantity()) {
            return createCheckResult(item, count, true, false, details.bonusQuantity(), 0);
        }

        if (availablePromotions > 0) {
            return createCheckResult(item, count, false, true, bonusCount, availablePromotions);
        }
        return null;
    }

    private PromotionCheck createCheckResult(Item item, int count,
                                             boolean hasBonus, boolean isExceeded, int bonus, int exceedCount) {
        return new PromotionCheck(
                item, count,
                hasBonus, isExceeded, bonus, exceedCount);
    }

}
