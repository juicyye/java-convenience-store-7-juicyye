package store.convenience.order.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderProduct> orderProducts = new ArrayList<>();
    private Discount discount;

    private Order() {
    }

    public static Order create(Discount discount, List<OrderProduct> orderProducts) {
        Order order = new Order();
        order.discount = discount;
        for (OrderProduct orderProduct : orderProducts) {
            order.addOrderProduct(orderProduct);
        }
        return order;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public int getTotalPrice(){
        return orderProducts.stream()
                .mapToInt(OrderProduct::getOrderPrice)
                .sum();
    }

}
