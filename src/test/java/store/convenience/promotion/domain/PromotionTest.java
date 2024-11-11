package store.convenience.promotion.domain;

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
import store.global.exception.ErrorMessage;

class PromotionTest {

    @Test
    @DisplayName("올바른 값을 넣으면 Promotion이 생성된다")
    void create() throws Exception {
        // given
        PromotionDetails details = getDetails("test", 2, 3);
        LocalDate startDate = getDate(1, 1);
        LocalDate endDate = getDate(12, 31);

        // when
        Promotion promotion = new Promotion(details, startDate, endDate);

        // then
        assertAll(() -> {
            assertThat(promotion.getStartDate()).isEqualTo(startDate);
            assertThat(promotion.getEndDate()).isEqualTo(endDate);
            assertThat(promotion.getDetails()).isEqualTo(details);
        });
    }

    @Test
    @DisplayName("EndDate가 StartDate보다 빠르면 에러를 반환한다")
    void invalidEndDate() throws Exception {
        // given
        PromotionDetails details = getDetails("test", 2, 3);
        LocalDate startDate = getDate(5, 5);
        LocalDate endDate = getDate(1, 27);

        // then
        assertThatThrownBy(() -> new Promotion(details, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_END_DATE.getMessage());
    }

    @ParameterizedTest
    @DisplayName("입력된 날짜에 따라 프로모션 활성 상태를 확인한다")
    @MethodSource("providedDate")
    void isActivePromotion(LocalDate inputValue, boolean expectResult) throws Exception {
        // given
        Promotion promotion = createPromotion(2, 1, 2, 1);

        // when
        boolean result = promotion.isActivePromotion(inputValue);

        // then
        assertThat(result).isEqualTo(expectResult);
    }

    private static Stream<Arguments> providedDate() {
        return Stream.of(
                Arguments.arguments(getDate(2, 1), true),
                Arguments.arguments(getDate(1, 31), false),
                Arguments.arguments(getDate(2, 2), false)
        );
    }

    @ParameterizedTest
    @DisplayName("주문과 프로모션에 따라 보너스 아이템을 얻기 위한 추가 구매수량을 알려준다")
    @MethodSource("providedRemaining")
    void checkFreeItemAvailability(Promotion promotion, int orderCount, int expect) throws Exception {
        // when
        int result = promotion.checkFreeItemAvailability(orderCount);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedRemaining() {
        return Stream.of(
                Arguments.arguments(createPromotion(5, 5, 2, 1), 2, 1),
                Arguments.arguments(createPromotion(5, 5, 3, 2), 3, 2),
                Arguments.arguments(createPromotion(5, 5, 3, 2), 4, 1),
                Arguments.arguments(createPromotion(5, 5, 1, 2), 1, 2),
                Arguments.arguments(createPromotion(5, 5, 1, 2), 2, 1),
                Arguments.arguments(createPromotion(5, 5, 1, 1), 1, 1),
                Arguments.arguments(createPromotion(5, 5, 1, 1), 3, 1),
                Arguments.arguments(createPromotion(5, 5, 4, 2), 10, 2)
        );

    }

    @ParameterizedTest
    @DisplayName("재고와 주문수량에 따라 보너스 수량을 알려준다")
    @MethodSource("providedBonus")
    void calculateBonus(Promotion promotion, int orderCount, int expect) throws Exception {
        // when
        int result = promotion.calculateBonus(orderCount);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> providedBonus() {
        return Stream.of(
                Arguments.arguments(createPromotion(5, 5, 2, 1), 3, 1),
                Arguments.arguments(createPromotion(5, 5, 3, 2), 8, 2),
                Arguments.arguments(createPromotion(5, 5, 1, 2), 4, 2)
        );

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

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}