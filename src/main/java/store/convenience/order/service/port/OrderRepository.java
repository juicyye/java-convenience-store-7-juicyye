package store.convenience.order.service.port;

import java.util.Optional;
import store.convenience.order.domain.Order;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findRecentOrder();

    void clear();

}