package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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

class OrderAdjustmentServiceTest {

    private LocalDate localDate = LocalDate.of(2024, 11, 11);

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final OrderAdjustmentService orderAdjustmentService = new OrderAdjustmentService();
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), localDate, localDate);
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), localDate, localDate);
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
    void calculateBonusQuantity() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.ORANGE_JUICE, 1);

        // when
        OrderCreateReqDto result = orderAdjustmentService.applyBonus(createReqDto, 1);

        // then
        assertThat(result.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("초과한 상품의 개수만큼 수량에서 뺀다")
    void excludeExceededQuantity() throws Exception {
        // given
        OrderCreateReqDto createReqDto = createReqDto(Item.ORANGE_JUICE, 5);

        // when
        OrderCreateReqDto result = orderAdjustmentService.excludeExceededQuantity(createReqDto, 2);

        // then
        assertThat(result.count()).isEqualTo(3);
    }

    private OrderCreateReqDto createReqDto(Item item, int count) {
        return new OrderCreateReqDto(item, count);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}