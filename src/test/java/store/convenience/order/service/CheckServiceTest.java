package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.controller.resp.PromotionCheckResult;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;
import store.mock.FakeDateTimeHolder;

class CheckServiceTest {

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private final FakeDateTimeHolder fakeDateTimeHolder = new FakeDateTimeHolder(
            LocalDateTime.of(2024, 5, 5, 12, 38, 45));
    private final CheckService checkService = new CheckService(productRepository, fakeDateTimeHolder);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails("탄산2+1", 2, 1), getDate(), getDate());
        Promotion promotion2 = new Promotion(getDetails("오렌지주스", 1, 1), getDate(), getDate());
        promotionRepository.save(promotion);
        promotionRepository.save(promotion2);
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
    @DisplayName("프로모션 이벤트 기간에 프로모션 수량을 넘어서 구매를 할 때 프로모션 할인 적용이 안되는 수량을 알려준다")
    void overPromotionCount() throws Exception {
        // given
        OrderCreateReqDto createReqDto = new OrderCreateReqDto("콜라", 10);

        // when
        List<PromotionCheckResult> result = checkService.checkPromotion(List.of(createReqDto));

        // then
        assertThat(result).hasSize(1)
                .extracting(PromotionCheckResult::count, PromotionCheckResult::isExceeded, PromotionCheckResult::freeItemCount)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(10,true,4)
                );
    }

    @Test
    @DisplayName("프로모션 수량에 맞춰 구매를 하면 무료로 받을 수 있는 수량을 알려준다")
    void canReceiveFreeItem() throws Exception {
        // given
        OrderCreateReqDto createReqDto = new OrderCreateReqDto("오렌지주스", 1);

        // when
        List<PromotionCheckResult> result = checkService.checkPromotion(List.of(createReqDto));

        // then
        assertThat(result).hasSize(1)
                .extracting(PromotionCheckResult::hasBonus, PromotionCheckResult::freeItemCount)
                .containsExactlyInAnyOrder(Tuple.tuple(true, 1));
    }

    @Test
    @DisplayName("프로모션 구매 조건에 충족하지 못하면 알려준다")
    void notFulfillTheRequirements() throws Exception {
        // given
        OrderCreateReqDto createReqDto = new OrderCreateReqDto("콜라", 1);

        // when
        List<PromotionCheckResult> result = checkService.checkPromotion(List.of(createReqDto));

        // then
        assertThat(result).hasSize(1)
                .extracting(PromotionCheckResult::isPromotion, PromotionCheckResult::freeItemCount)
                .containsExactlyInAnyOrder(Tuple.tuple(false,0));
    }

    private LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}