package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static store.global.util.StoreConstant.MAX_MEMBERSHIP_DISCOUNT;
import static store.global.util.StoreConstant.MEMBERSHIP_RATE;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.domain.Discount;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;

class OrderPromotionServiceTest {

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final OrderPromotionService orderPromotionService = new OrderPromotionService(productRepository);
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();

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
        productRepository.clear();
        promotionRepository.clear();
    }

    @Test
    @DisplayName("보너스 상품을 추가하면 orderCount가 +1 더해진다")
    void 이름_test() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto("오렌지주스", 1, getDate());

        // when
        OrderCreateReqDto result = orderPromotionService.applyBonus(createReqDto,1);

        // then
        assertThat(result.count()).isEqualTo(2);
    }

    @ParameterizedTest
    @DisplayName("주문에 대한 할인 금액을 알 수 있다")
    @MethodSource("providedOrderData")
    void OrderDiscount(List<OrderCreateReqDto> orderCreateReqDtos, int expect) throws Exception {
        // when
        Discount discount = orderPromotionService.calculateOrderDiscount(orderCreateReqDtos, true);

        // then
        assertThat(discount.getPromotionDiscount()).isEqualTo(expect);
    }

    private static Stream<Arguments> providedOrderData(){
        return Stream.of(
                Arguments.arguments(List.of(createReqDto("오렌지주스", 4, getDate())), 3600),
                Arguments.arguments(List.of(createReqDto("콜라", 2, getDate())), 1000),
                Arguments.arguments(List.of(createReqDto("콜라", 4, getDate())), 2000)
        );
    }

    private static  OrderCreateReqDto createReqDto(String itemName, int count, LocalDate date) {
        return new OrderCreateReqDto(itemName, count, date);
    }

    private static LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}