package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;
import store.mock.FakeLocalDateTimeHolder;

class OrderPromotionServiceTest {
    private LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 11, 12, 45);
    private LocalDate localDate = LocalDate.of(2024, 11, 11);

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private final FakeLocalDateTimeHolder fakeDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
    private final OrderPromotionService orderPromotionService =
            new OrderPromotionService(productRepository, fakeDateTimeHolder);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), localDate, localDate);
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), localDate, localDate);
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

    @Test
    @DisplayName("주문 상품이 프로모션 상품인지 프로모션 기한내에 있는지 확인한다")
    void orderPromotion() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.COLA, 10);

        // when
        boolean result = orderPromotionService.checkPromotion(createReqDto);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("프로모션 이벤트 기간에 프로모션 수량을 넘어서 구매를 할 때 프로모션 할인 적용이 안되는 수량을 알려준다")
    void overPromotionCount() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.COLA, 10);

        // when
        int result = orderPromotionService.determineExcessQuantity(createReqDto);

        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("프로모션 기간과 프로모션 구매 조건에 맞으면 무료 증정 아이템 수량을 알려준다")
    void getEligibleBonusItemCount() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.COLA, 5);

        // when
        int result = orderPromotionService.getEligibleBonusItemCount(createReqDto);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("프로모션 기간과 구매 수량에 따른 무료 증정 아이템 수량을 알려준다")
    void determineBonusQuantity() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.COLA, 7);

        // when
        int result = orderPromotionService.determineBonusQuantity(createReqDto, localDate);

        // then
        assertThat(result).isEqualTo(2);
    }

    private static OrderCreateReqDto createReqDto(Item item, int count) {
        return new OrderCreateReqDto(item, count);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}