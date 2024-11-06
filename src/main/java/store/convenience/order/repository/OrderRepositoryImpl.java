package store.convenience.order.repository;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.domain.Order;
import store.convenience.order.service.port.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {

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
