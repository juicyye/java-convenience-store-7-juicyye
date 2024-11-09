package store.convenience.promotion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.promotion.domain.Promotion;
import store.global.exception.ErrorMessage;

class PromotionRequestMapperTest {

    private PromotionRequestMapper mapper = PromotionRequestMapper.getInstance();


    @Test
    @DisplayName("정확한 입력이 주어지면 promotion이 정상적으로 생성된다")
    void createPromotion() throws Exception {
        // given
        String[] input = {"탄산2+1", "2", "1", "2024-01-01", "2024-12-31"};

        // when
        Promotion result = mapper.create(input);

        // then
        assertAll(() -> {
            assertThat(result.getEndDate()).isEqualTo("2024-12-31");
            assertThat(result.getStartDate()).isEqualTo("2024-01-01");
            assertThat(result.getDetails().name()).isEqualTo("탄산2+1");
            assertThat(result.getDetails().bonusQuantity()).isEqualTo(1);
            assertThat(result.getDetails().purchaseQuantity()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("유효하지 않은 날짜라면 에러를 반환한다")
    void InvalidDate() throws Exception {
        // given
        String[] input = {"탄산2+1", "2", "1", "2024-01-01-03", "2024-12-31"};

        // then
        assertThatThrownBy(() -> mapper.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
    }

}