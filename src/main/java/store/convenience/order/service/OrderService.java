package store.convenience.order.service;

import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Order;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.PromotionDetails;
import store.global.exception.NotEnoughStockException;

public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void processOrder(List<OrderCreateReqDto> orderCreateReqDtos) {
        for (OrderCreateReqDto createReqDto : orderCreateReqDtos) {
            List<Product> products = productRepository.findByName(createReqDto.itemName());
            adjustStock(createReqDto.count(), products);
            orderRepository.save(new Order(products, createReqDto.count(),0));
        }
    }

    private void adjustStock(int count, List<Product> products) {
        for (Product product : products) {
            if (product.getPromotion() != null) {
                count = orderPromotion(count, product);
            }
            product.removeStock(count);
        }
    }

    private int orderPromotion(int count, Product product) {
        PromotionDetails details = product.getPromotion().getDetails();
        try {
            if (count == details.purchaseQuantity()) {
                product.removeStock(details.bonusQuantity());
            }
        } catch (NotEnoughStockException e) {
            count += details.bonusQuantity();
        }
        return count;
    }

}
