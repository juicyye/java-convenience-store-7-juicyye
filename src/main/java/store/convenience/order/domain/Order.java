package store.convenience.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private List<OrderProduct> orderProducts = new ArrayList<>();
    private Discount discount;
    private LocalDateTime createdAt;

    private Order() {
    }

    public static Order create(Discount discount, List<OrderProduct> orderProducts, LocalDateTime createdAt) {
        Order order = new Order();
        order.discount = discount;
        order.createdAt = createdAt;
        for (OrderProduct orderProduct : orderProducts) {
            order.addOrderProduct(orderProduct);
        }
        return order;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public int getTotalOrderCount() {
        return orderProducts.stream()
                .mapToInt(OrderProduct::getCount)
                .sum();
    }

    public int getTotalPrice() {
        return orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
    }

    public List<OrderProduct> getOrderProducts() {
        return Collections.unmodifiableList(orderProducts);
    }

    public Discount getDiscount() {
        return discount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}