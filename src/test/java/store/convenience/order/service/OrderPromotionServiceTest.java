package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;
import store.mock.FakeDateTimeHolder;

class OrderPromotionServiceTest {

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private final FakeDateTimeHolder fakeDateTimeHolder = new FakeDateTimeHolder(
            LocalDateTime.of(2024, 5, 5, 12, 38, 45));
    private final OrderPromotionService orderPromotionService = new OrderPromotionService(productRepository);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), getDate(), getDate());
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), getDate(), getDate());
        promotionRepository.save(promotion);
        promotionRepository.save(promotion2);
        Product product = new Product(Item.COLA, 7, promotion);
        Product product2 = new Product(Item.ORANGE_JUICE, 9, promotion2);
        Product product3 = new Product(Item.CIDER, 9, null);
        productRepository.save(product);
        productRepository.save(product2);
        productRepository.save(product3);
    }

    @AfterEach
    void tearDown() {
        productRepository.clear();
        promotionRepository.clear();
    }

    @ParameterizedTest
    @DisplayName("주문 상품이 프로모션 상품인지 프로모션 기한내에 있는지 확인한다")
    @MethodSource("providedOrderProduct")
    void orderPromotion(OrderCreateReqDto createReqDto, boolean expect) throws Exception {
        // when
        boolean result = orderPromotionService.checkPromotion(createReqDto);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedOrderProduct() {
        return Stream.of(
                Arguments.arguments(createReqDto("콜라", 3, getDate()), true),
                Arguments.arguments(createReqDto("사이다", 3, getDate()), false),
                Arguments.arguments(createReqDto("콜라", 3, LocalDate.of(2024, 5, 6)), false)
        );
    }

    @Test
    @DisplayName("프로모션 이벤트 기간에 프로모션 수량을 넘어서 구매를 할 때 프로모션 할인 적용이 안되는 수량을 알려준다")
    void overPromotionCount() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto("콜라", 10, getDate());

        // when
        int result = orderPromotionService.calculateExcessQuantity(createReqDto);

        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("프로모션 수량에 맞춰 구매를 하면 무료로 받을 수 있는 수량을 알려준다")
    void canReceiveFreeItem() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto("오렌지주스", 3, getDate());

        // when
        int result = orderPromotionService.calculateBonusQuantity(createReqDto);

        // then
        assertThat(result).isEqualTo(1);
    }

    private static OrderCreateReqDto createReqDto(String itemName, int count, LocalDate date) {
        return new OrderCreateReqDto(itemName, count, date);
    }

    @Test
    @DisplayName("프로모션 이벤트 ")
    void 이름_calculateRemaining() throws Exception {
        // given

        // when

        // then
    }

    private static LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}