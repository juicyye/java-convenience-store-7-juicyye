package store.convenience.order.infrastructure;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.domain.Order;
import store.convenience.order.service.port.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {

    private static final OrderRepositoryImpl instance = new OrderRepositoryImpl();

    private OrderRepositoryImpl() {}

    public static OrderRepository getInstance() {
        return instance;
    }

    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findAll() {
        return orders;
    }
}
