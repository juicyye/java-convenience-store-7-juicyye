package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.Order;
import store.convenience.order.domain.OrderProduct;
import store.convenience.order.infrastructure.OrderRepositoryImpl;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;

class OrderServiceTest {

    private OrderRepository orderRepository = OrderRepositoryImpl.getInstance();
    private ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private OrderPromotionService orderPromotionService = new OrderPromotionService(productRepository);
    private final DiscountService discountService = new DiscountService(orderPromotionService);
    private OrderService orderService = new OrderService(orderRepository, productRepository,discountService);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), getDate(), getDate());
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), getDate(), getDate());
        Product product = new Product(Item.COLA, 7, promotion);
        Product product2 = new Product(Item.ORANGE_JUICE, 9, promotion2);
        productRepository.save(product);
        productRepository.save(product2);
    }

    @AfterEach
    void tearDown() {
        orderRepository.clear();
        productRepository.clear();
    }

    @Test
    @DisplayName("올바른 값을 입력하면 주문을 할 수 있다")
    void createProcess() throws Exception {
        // given
        List<OrderCreateReqDto> request = List.of(createReqDto("오렌지주스", 4, getDate()));
        orderService.process(request, true);

        // when
        List<Order> results = orderRepository.findAll();

        // then
        assertThat(results)
                .flatExtracting(Order::getOrderProducts)
                .extracting(
                        orderProduct -> orderProduct.getProduct().getItem(),
                        OrderProduct::getCount
                )
                .containsExactlyInAnyOrder(
                        tuple(Item.ORANGE_JUICE, 4)
                );
    }

    @Test
    @DisplayName("프로모션 기간에 주문하면 discount에 할인 금액이 저장된다")
    void getDiscount() throws Exception {
        // given
        List<OrderCreateReqDto> request = List.of(createReqDto("오렌지주스", 4, getDate()));
        orderService.process(request, true);

        // when
        List<Order> results = orderRepository.findAll();

        // then
        assertThat(results)
                .extracting(Order::getDiscount)
                .extracting(Discount::getPromotionDiscount, Discount::getMembershipDiscount, Discount::getTotalDiscount)
                .containsExactlyInAnyOrder(
                        tuple(3600, (int) Math.floor(3600 * 0.3) / 100 * 100,4600)
                );
    }

    private OrderCreateReqDto createReqDto(String itemName, int count, LocalDate date) {
        return new OrderCreateReqDto(itemName, count, date);
    }

    private LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}