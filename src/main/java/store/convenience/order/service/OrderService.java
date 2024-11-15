package store.convenience.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.order.service.port.LocalDateTimeHolder;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.global.exception.ErrorMessage;
import store.global.exception.NotEnoughStockException;
import store.global.exception.NotFoundException;

public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final LocalDateTimeHolder localDateTimeHolder;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository, DiscountService discountService,
                        LocalDateTimeHolder localDateTimeHolder) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
        this.localDateTimeHolder = localDateTimeHolder;
    }

    public void process(List<OrderCreateReqDto> createReqDtos, boolean hasMembership) {
        LocalDateTime currentDate = localDateTimeHolder.now();
        Discount discount = processDiscount(createReqDtos, hasMembership, currentDate.toLocalDate());

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderCreateReqDto createReqDto : createReqDtos) {
            handlerOrder(createReqDto, orderProducts, currentDate);
        }

        orderRepository.save(Order.create(discount, orderProducts, currentDate));
    }

    private void handlerOrder(OrderCreateReqDto createReqDto, List<OrderProduct> orderProducts,
                              LocalDateTime currentTime) {
        Product product = getProduct(createReqDto.item().getName());
        if (product.canApplyPromotion(currentTime.toLocalDate())) {
            processPromotionOrder(createReqDto.count(), product, orderProducts);
        }

        if (!product.canApplyPromotion(currentTime.toLocalDate())) {
            processRegularOrder(createReqDto.count(), product, orderProducts);
        }
    }

    private Discount processDiscount(List<OrderCreateReqDto> createReqDtos, boolean hasMembership,
                                     LocalDate currentDate) {
        return discountService.calculateOrderDiscount(createReqDtos, hasMembership,
                currentDate);

    }

    private void processPromotionOrder(int orderCount, Product product, List<OrderProduct> orderProducts) {
        try {
            orderProducts.add(OrderProduct.create(product, product.getItem().getPrice(), orderCount));
        } catch (NotEnoughStockException e) {
            product.removeStock(product.getQuantity());
            orderCount -= product.getQuantity();
            product = getNoPromotionProduct(product.getItem().getName());
            processRegularOrder(orderCount, product, orderProducts);
        }
    }

    private void processRegularOrder(int orderCount, Product product, List<OrderProduct> orderProducts) {
        orderProducts.add(
                OrderProduct.create(product, product.getItem().getPrice(), orderCount)
        );
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

    public Order getLatestOrder() {
        return orderRepository.findRecentOrder().orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND_ORDER.getMessage()));
    }

}