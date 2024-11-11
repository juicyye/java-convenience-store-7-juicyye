package store.convenience.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.promotion.domain.Promotion;

class OrderTest {

    @Test
    @DisplayName("올바른 값을 입력하면 오더를 생성할 수 있다")
    void createOrder() throws Exception {
        // given
        List<OrderProduct> orderProducts = List.of(getOrderProducts(Item.COLA, 3, 2));
        Discount discount = getDiscount();
        LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 11, 13, 02);

        // when
        Order result = Order.create(discount, orderProducts, localDateTime);

        // then
        assertThat(result)
                .extracting(Order::getDiscount, Order::getOrderProducts, Order::getCreatedAt)
                .containsExactly(discount, orderProducts, localDateTime);
    }

    @Test
    @DisplayName("총 주문 수량을 알 수 있다")
    void getTotalOrderCount() throws Exception {
        // given
        Discount discount = getDiscount();
        OrderProduct orderProduct1 = getOrderProducts(Item.ORANGE_JUICE, 4000,  2);
        OrderProduct orderProduct2 = getOrderProducts(Item.COLA, 2000,  3);
        List<OrderProduct> orderProducts = List.of(orderProduct1, orderProduct2);
        Order order = Order.create(discount, orderProducts, LocalDateTime.of(2024, 11, 11, 13, 02));

        // when
        int result = order.getTotalOrderCount();

        // then
        assertThat(result).isEqualTo(5);
    }

    @Test
    @DisplayName("총 주문 가격을 알 수 있다")
    void getTotalPrice() throws Exception {
        // given
        Discount discount = getDiscount();
        OrderProduct orderProduct1 = getOrderProducts(Item.ORANGE_JUICE, 4000,  2);
        OrderProduct orderProduct2 = getOrderProducts(Item.COLA, 2000,  3);
        List<OrderProduct> orderProducts = List.of(orderProduct1, orderProduct2);
        Order order = Order.create(discount, orderProducts, LocalDateTime.of(2024, 11, 11, 13, 02));

        // when
        int result = order.getTotalPrice();

        // then
        assertThat(result).isEqualTo(14000);
    }

    private OrderProduct getOrderProducts(Item item, int orderPrice, int orderCount) {
        return OrderProduct.create(new Product(item, 10, new Promotion(null, LocalDate.now(), LocalDate.now())),
                orderPrice, orderCount);
    }

    private Discount getDiscount() {
        return new Discount(List.of(new ItemCount(Item.COLA, 3)), 30, 20);
    }


}