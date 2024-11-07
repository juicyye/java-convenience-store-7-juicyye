package store.convenience.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.NotEnoughStockException;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderPromotionService orderPromotionService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderPromotionService orderPromotionService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderPromotionService = orderPromotionService;
    }

    public void order(List<OrderCreateReqDto> createReqDtos, boolean hasMembership) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            Product product = getProduct(createReqDto.itemName());
            int orderCount = createReqDto.count();

            if (isPromotionActive(product.getPromotion(), createReqDto.currentDate())) {
                try {
                    orderProducts.add(createOrderProduct(product, product.getItem().getPrice(), orderCount));
                    continue;
                } catch (NotEnoughStockException e) {
                    product.removeStock(product.getQuantity());
                    orderCount -= product.getQuantity();
                    product = getProduct(createReqDto.itemName());
                }
            }
            orderProducts.add(createOrderProduct(product, product.getItem().getPrice(), orderCount));
        }

        Discount discount = orderPromotionService.calculateOrderDiscount(createReqDtos, hasMembership);

        orderRepository.save(Order.create(discount, orderProducts));
    }

    private boolean isPromotionActive(Promotion promotion, LocalDate currentDate) {
        return promotion != null && promotion.isActivePromotion(currentDate);
    }

    private OrderProduct createOrderProduct(Product product, int price, int orderCount) {
        return OrderProduct.create(product,
                price,
                orderCount);
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

}
