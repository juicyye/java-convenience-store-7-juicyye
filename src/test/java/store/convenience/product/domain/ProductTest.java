package store.convenience.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.global.exception.NotEnoughStockException;
import store.global.util.ErrorMessage;

class ProductTest {

    @Test
    @DisplayName("주문을 하면 product의 재고가 감소 된다")
    void order() throws Exception {
        // given
        Product product = createProduct(Item.COLA, 10, createPromotion(5, 5, 2, 1));

        // when
        product.removeStock(5);

        // then
        assertAll(() -> {
            assertThat(product.getQuantity()).isEqualTo(5);
            assertThat(product.getItem()).isEqualByComparingTo(Item.COLA);
        });
    }

    @Test
    @DisplayName("재고를 초과한 주문을 하면 에러를 반환한다")
    void OverQuantityError() throws Exception {
        // given
        Product product = createProduct(Item.COLA, 10, createPromotion(5, 5, 2, 1));

        // then
        assertThatThrownBy(() -> product.removeStock(11))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage(ErrorMessage.OUT_OF_STOCK.getMessage());
    }

    @ParameterizedTest
    @DisplayName("프로모션 활성화에 따라 얼마를 구매해야 프로모션을 받을 수 있는지 확인한다")
    @MethodSource("providedAddCount")
    void remainingForBonus(Product product, LocalDate currentDate, int orderCount, int expect) throws Exception {
        // when
        int result = product.remainingForBonus(orderCount, currentDate);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedAddCount() {
        return Stream.of(
                Arguments.arguments(
                        createProduct(Item.COLA, 10, createPromotion(5, 5, 2, 1)),
                        getDate(5, 5), 2, 1),
                Arguments.arguments(
                        createProduct(Item.COLA, 10, createPromotion(5, 5, 2, 1)),
                        getDate(5, 6), 2, 0),
                Arguments.arguments(
                        createProduct(Item.COLA, 10, null),
                        getDate(5, 5), 2, 0)
        );
    }

    @ParameterizedTest
    @DisplayName("프로모션 활성화와 프로모션 재고에 따른 초과한 수량을 알려준다")
    @MethodSource("providedExcessData")
    void calculateExcessQuantity(Product product, LocalDate currentDate, int orderCount, int expect) throws Exception {
        // when
        int result = product.calculateExcessQuantity(orderCount, currentDate);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedExcessData() {
        return Stream.of(
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 5), 10, 4),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 6), 10, 0),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, null),
                        getDate(5, 5), 10, 0)
        );
    }


    @ParameterizedTest
    @DisplayName("프로모션 활성화에 따른 무료 증정 아이템의 개수를 확인한다")
    @MethodSource("providedBonusQuantity")
    void getBonusQuantity(Product product, LocalDate currentDate, int expect) throws Exception {
        // when
        int result = product.getBonusQuantity(currentDate);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedBonusQuantity() {
        return Stream.of(
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 5), 1),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 6), 0),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, null),
                        getDate(5, 5), 0)
        );
    }

    @ParameterizedTest
    @DisplayName("프로모션 활성화와 주문개수에 따른 무료 증정 아이템 개수를 확인한다")
    @MethodSource("providedOrderBonus")
    void calculateBonusQuantity(Product product, LocalDate targetDate, int orderCount, int expect) throws Exception {
        // when
        int result = product.calculateBonusQuantity(orderCount, targetDate);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedOrderBonus() {
        return Stream.of(
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 5), 6, 2),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, createPromotion(5, 5, 2, 1)),
                        getDate(5, 5), 5, 1),
                Arguments.arguments(
                        createProduct(Item.COLA, 7, null),
                        getDate(5, 6), 6,0)
        );
    }

    private static Product createProduct(Item item, int quantity, Promotion promotion) {
        return new Product(item, quantity, promotion);
    }

    private static Promotion createPromotion(int promotionMonth, int promotionDay, int purchaseQuantity,
                                             int bonusQuantity) {
        PromotionDetails details = new PromotionDetails("test", purchaseQuantity, bonusQuantity);
        LocalDate startDate = getDate(promotionMonth, promotionDay);
        LocalDate endDate = getDate(promotionMonth, promotionDay);
        return new Promotion(details, startDate, endDate);
    }

    private static LocalDate getDate(int month, int dayOfMonth) {
        return LocalDate.of(2024, month, dayOfMonth);
    }

}