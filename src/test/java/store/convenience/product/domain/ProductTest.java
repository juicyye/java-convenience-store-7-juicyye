package store.convenience.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;

class ProductTest {

    /*@Test
    @DisplayName("프로모션 무료 증정 아이템의 개수를 확인한다")
    void calculateBonusQuantity() throws Exception {
        // given
        int orderCount = 6;
        int quantity = 7;
        LocalDate currentDate = getDate(5, 5);
        Promotion promotion = createPromotion(5, 5, 2, 1);

        // when
        int result = promotion.calculateBonusQuantity(orderCount, quantity, currentDate);

        // then
        assertThat(result).isEqualTo(2);
    }

    private Stream<Arguments> providedBonusQuantity() {
        return Stream.of(
                Arguments.arguments(6,7)
        )
    }*/

    private static Promotion createPromotion(int promotionMonth, int promotionDay, int purchaseQuantity, int bonusQuantity) {
        PromotionDetails details = new PromotionDetails("test", purchaseQuantity, bonusQuantity);
        LocalDate startDate = getDate(promotionMonth, promotionDay);
        LocalDate endDate = getDate(promotionMonth, promotionDay);
        return new Promotion(details, startDate, endDate);
    }

    private static LocalDate getDate(int month, int dayOfMonth) {
        return LocalDate.of(2024, month, dayOfMonth);
    }

}