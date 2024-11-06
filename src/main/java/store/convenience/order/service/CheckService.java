package store.convenience.order.service;

import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.service.port.DateTimeHolder;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;

public class CheckService {

    private final ProductRepository productRepository;
    private final DateTimeHolder dateTimeHolder;

    public CheckService(ProductRepository productRepository,
                        DateTimeHolder dateTimeHolder) {
        this.productRepository = productRepository;
        this.dateTimeHolder = dateTimeHolder;
    }

    public int checkPromotion(List<OrderCreateReqDto> createReqDtos) {
        int bonus = 0;
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            List<Product> products = productRepository.findByName(createReqDto.itemName());
            for (Product product : products) {
                if (product.getPromotion() != null) {
                    Promotion promotion = product.getPromotion();
                    if (!promotion.isActivePromotion(dateTimeHolder.now().toLocalDate())) {
                        break;
                    }

                    int promotions = product.getPromotion().totalPromotions();
                    int count = createReqDto.count();
                    int i = count % promotions;
                    bonus = count - promotions * i;
                }
            }
        }
        return bonus;
    }

}
