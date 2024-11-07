package store.convenience.order.service.port;

import java.util.List;
import store.convenience.order.domain.Order;

public interface OrderRepository {
    void save(Order order);

    List<Order> findAll();

    void clear();

}
