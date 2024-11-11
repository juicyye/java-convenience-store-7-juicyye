package store.convenience.order.infrastructure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import store.convenience.order.domain.Order;
import store.convenience.order.service.port.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {

    private static final OrderRepository instance = new OrderRepositoryImpl();
    private final List<Order> orders = new ArrayList<>();

    private OrderRepositoryImpl() {
    }

    public static OrderRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public Optional<Order> findRecentOrder() {
        return orders.stream()
                .max(Comparator.comparing(Order::getCreatedAt));
    }

    @Override
    public void clear() {
        orders.clear();
    }

}