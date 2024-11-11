package store.convenience.order.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.ItemCount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.promotion.domain.Promotion;

class OrderRepositoryImplTest {

    private OrderRepository orderRepository = OrderRepositoryImpl.getInstance();

    @AfterEach
    void tearDown() {
        orderRepository.clear();
    }

    @Test
    @DisplayName("최근에 주문한 내역을 찾을 수 있다")
    void findRecentOrder() throws Exception {
        // given
        Discount discount = getDiscount();
        List<OrderProduct> orderProducts = getOrderProducts();

        Order order1 = Order.create(discount, orderProducts, LocalDateTime.of(2024,11,11,12,00));
        Order order2 = Order.create(discount, orderProducts, LocalDateTime.of(2024,11,11,12,01));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        Order result = orderRepository.findRecentOrder().orElse(null);

        // then
        assertThat(result).isNotNull()
                .extracting(Order::getCreatedAt)
                .isEqualTo(LocalDateTime.of(2024,11,11,12,01));
    }

    private static List<OrderProduct> getOrderProducts() {
        return List.of(OrderProduct.create(new Product(Item.COLA, 3, new Promotion(null, LocalDate.now(), LocalDate.now())), 3, 2));
    }

    private static Discount getDiscount() {
        return new Discount(List.of(new ItemCount(Item.COLA, 3)), 30, 20);
    }

}