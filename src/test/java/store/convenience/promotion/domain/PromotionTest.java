package store.convenience.promotion.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.util.ErrorMessage;

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

    private LocalDate getDate(int month, int dayOfMonth) {
        return LocalDate.of(2024, month, dayOfMonth);
    }

    private PromotionDetails getDetails(String name, int buy, int get) {
        return new PromotionDetails(name, buy, get);
    }

}