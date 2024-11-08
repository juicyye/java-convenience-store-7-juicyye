package store.convenience.promotion.infrastrcuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.service.port.PromotionRepository;

class PromotionRepositoryImplTest {

    private PromotionRepository repository = PromotionRepositoryImpl.getInstance();

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    @DisplayName("promotion을 저장하고 정상적으로 조회할 수 있다")
    void saveAndFetchPromotion() throws Exception {
        // given
        String promotionName = "프로모션";
        int buy = 2;
        int get = 1;
        LocalDate startDate = LocalDate.of(2024, 5, 5);
        LocalDate endDate = LocalDate.of(2024, 10, 2);
        Promotion promotion = new Promotion(new PromotionDetails(promotionName, buy, get), startDate, endDate);
        repository.save(promotion);

        // when
        Promotion result = repository.findByName(promotionName).orElse(null);

        // then
        assertAll(() -> {
            assertThat(result.getEndDate()).isEqualTo(endDate);
            assertThat(result.getStartDate()).isEqualTo(startDate);
            assertThat(result.getDetails().name()).isEqualTo(promotionName);
            assertThat(result.getDetails().bonusQuantity()).isEqualTo(get);
            assertThat(result.getDetails().purchaseQuantity()).isEqualTo(buy);
        });
    }

}