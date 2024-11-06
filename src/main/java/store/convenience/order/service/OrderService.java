package store.convenience.order.service;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.order.domain.PromotionCheck;
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

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void order(List<PromotionCheck> promotionChecks, Discount discount) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (PromotionCheck promotionCheck : promotionChecks) {
            Product product = promotionCheck.getProduct();
            int orderCount = promotionCheck.getCount();
            
            if (isPromotionActive(product.getPromotion(), promotionCheck.isPromotion())) {
                try {
                    orderProducts.add(createOrderProduct(product, product.getItem().getPrice(), orderCount));
                    continue;
                } catch (NotEnoughStockException e) {
                    product.removeStock(product.getQuantity());
                    orderCount -= product.getQuantity();
                    product = getProduct(product);
                }
            }

            orderProducts.add(createOrderProduct(product, product.getItem().getPrice(), orderCount));
        }
        orderRepository.save(Order.create(discount, orderProducts));
    }

    private boolean isPromotionActive(Promotion promotion, boolean isPromotion) {
        return promotion != null && isPromotion;
    }

    private OrderProduct createOrderProduct(Product product, int price, int orderCount) {
        return OrderProduct.create(product,
                price,
                orderCount);
    }

    private Product getProduct(Product product) {
        return productRepository.findByNameAndNoPromotion(product.getItem().getName())
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

}
