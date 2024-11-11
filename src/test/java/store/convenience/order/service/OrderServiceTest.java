package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import store.global.exception.ErrorMessage;
import store.global.exception.NotEnoughStockException;
import store.mock.FakeLocalDateTimeHolder;

class OrderServiceTest {
    private LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 11, 12, 48);
    private LocalDate localDate = LocalDate.of(2024, 11, 11);

    private OrderRepository orderRepository = OrderRepositoryImpl.getInstance();
    private ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
    private OrderPromotionService orderPromotionService =
            new OrderPromotionService(productRepository, fakeLocalDateTimeHolder);
    private final DiscountService discountService = new DiscountService(orderPromotionService);
    private OrderService orderService = new OrderService(orderRepository, productRepository,
            discountService, fakeLocalDateTimeHolder);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), localDate, localDate);
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), localDate, localDate);
        Product colaPromotion = new Product(Item.COLA, 7, promotion);
        Product colaNoPromotion = new Product(Item.COLA, 1, null);
        Product product2 = new Product(Item.ORANGE_JUICE, 9, promotion2);
        productRepository.save(colaPromotion);
        productRepository.save(colaNoPromotion);
        productRepository.save(product2);
    }

    @AfterEach
    void tearDown() {
        orderRepository.clear();
        productRepository.clear();
    }

    @Test
    @DisplayName("올바른 값을 입력하면 주문이 저장된다")
    void createProcess() throws Exception {
        // given
        List<OrderCreateReqDto> request = List.of(createReqDto(Item.ORANGE_JUICE, 4));
        orderService.process(request, true);

        // when
        Order result = orderRepository.findRecentOrder().orElse(null);

        // then
        assertThat(result.getOrderProducts())
                .extracting(
                        orderProduct -> orderProduct.getProduct().getItem()
                        , OrderProduct::getCount)
                .containsExactlyInAnyOrder(
                        tuple(Item.ORANGE_JUICE, 4)
                );
    }

    @Test
    @DisplayName("프로모션 기간에 주문하면 discount에 할인 금액이 저장된다")
    void getDiscount() throws Exception {
        // given
        List<OrderCreateReqDto> request = List.of(createReqDto(Item.ORANGE_JUICE, 4));
        orderService.process(request, true);

        // when
        Order result = orderRepository.findRecentOrder().orElse(null);

        // then
        assertThat(result).isNotNull()
                .extracting(Order::getDiscount)
                .extracting(Discount::getPromotionDiscount, Discount::getMembershipDiscount, Discount::getTotalDiscount)
                .containsExactlyInAnyOrder(
                        3600, (int) Math.floor(3600 * 0.3) / 100 * 100, 4600
                );
    }

    @Test
    @DisplayName("재고 수량을 초과하여 주문하면 에러를 반환한다")
    void overStockError() throws Exception {
        // given
        List<OrderCreateReqDto> request = List.of(createReqDto(Item.COLA, 10));

        // then
        assertThatThrownBy(() -> orderService.process(request, true))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage(ErrorMessage.OUT_OF_STOCK.getMessage());
    }

    private OrderCreateReqDto createReqDto(Item item, int count) {
        return new OrderCreateReqDto(item, count);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}