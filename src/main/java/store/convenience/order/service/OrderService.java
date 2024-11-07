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
    private final DiscountService discountService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository, DiscountService discountService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
    }

    public void order(List<OrderCreateReqDto> createReqDtos, boolean hasMembership) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            Product product = getProduct(createReqDto.itemName());
            int orderCount = createReqDto.count();

            if (isPromotionActive(product.getPromotion(), createReqDto.currentDate())) {
                try {
                    orderProducts.add(OrderProduct.create(product, product.getItem().getPrice(), orderCount));
                    continue;
                } catch (NotEnoughStockException e) {
                    product.removeStock(product.getQuantity());
                    orderCount -= product.getQuantity();
                    product = getNoPromotionProduct(createReqDto.itemName());
                }
            }
            orderProducts.add(OrderProduct.create(product, product.getItem().getPrice(), orderCount));
        }

        Discount discount = discountService.calculateOrderDiscount(createReqDtos, hasMembership);

        orderRepository.save(Order.create(discount, orderProducts));
    }

    private boolean isPromotionActive(Promotion promotion, LocalDate currentDate) {
        return promotion != null && promotion.isActivePromotion(currentDate);
    }

    private Product getNoPromotionProduct(String itemName) {
        return productRepository.findByNameAndNoPromotion(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()
                ));
    }

    private Product getProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
