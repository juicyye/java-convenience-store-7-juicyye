package store.convenience.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.controller.resp.PromotionCheckResult;
import store.convenience.order.service.port.DateTimeHolder;
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

    public List<PromotionCheckResult> checkPromotion(List<OrderCreateReqDto> createReqDtos) {
        List<PromotionCheckResult> results = new ArrayList<>();

        for (OrderCreateReqDto createReqDto : createReqDtos) {
            List<Product> products = productRepository.findByName(createReqDto.itemName());
            checkProductPromotion(createReqDto, products, results);
        }
        return results;
    }

    private void checkProductPromotion(OrderCreateReqDto createReqDto, List<Product> products,
                                       List<PromotionCheckResult> results) {
        for (Product product : products) {
            PromotionCheckResult result = checkSinglePromotion(product, createReqDto);
            if (result != null) {
                results.add(result);
            }
        }
    }

    private PromotionCheckResult checkSinglePromotion(Product product, OrderCreateReqDto createReqDto) {
        if (!isValidPromotion(product, dateTimeHolder.now().toLocalDate())) {
            return null;
        }

        PromotionDetails details = product.getPromotion().getDetails();
        int count = createReqDto.count();
        int availablePromotions = calculateAvailablePromotions(product.getQuantity(), count, details);

        return calculatePromotionResult(createReqDto.itemName(), count, details, availablePromotions);
    }

    private boolean isValidPromotion(Product product, LocalDate now) {
        return product.getPromotion() != null && product.getPromotion().isActivePromotion(now);
    }

    private int calculateAvailablePromotions(int quantity, int count, PromotionDetails details) {
        int maxPromotions = quantity / details.totalPromotions();
        return count - details.totalPromotions() * maxPromotions;
    }

    private PromotionCheckResult calculatePromotionResult(String itemName, int count, PromotionDetails details,
                                                          int availablePromotions) {
        if (count == details.purchaseQuantity()) {
            return createCheckResult(itemName, count, true, false, details.bonusQuantity());
        }

        if (availablePromotions > 0) {
            return createCheckResult(itemName, count, false, true, availablePromotions);
        }
        return null;
    }

    private PromotionCheckResult createCheckResult(String itemName, int count,
                                                   boolean hasBonus, boolean isExceeded, int bonus) {
        return new PromotionCheckResult(
                itemName, count,
                hasBonus, isExceeded, bonus);
    }

}
