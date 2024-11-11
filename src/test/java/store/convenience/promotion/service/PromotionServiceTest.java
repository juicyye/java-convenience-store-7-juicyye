package store.convenience.promotion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;

class PromotionServiceTest {

    private PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private PromotionService promotionService = new PromotionService(promotionRepository);

    @AfterEach
    void tearDown() {
        promotionRepository.clear();
    }

    @Test
    @DisplayName("정확한 입력이 주어지면 Promotion이 정상적으로 저장된다")
    void savePromotion() throws Exception {
        // given
        String input = "탄산2+1,2,1,2024-01-01,2024-12-31";

        // when
        promotionService.create(input);
        Promotion result = promotionRepository.findByName("탄산2+1").orElse(null);

        // then
        assertAll(() -> {
            assertThat(result.getEndDate()).isEqualTo("2024-12-31");
            assertThat(result.getStartDate()).isEqualTo("2024-01-01");
            assertThat(result.getDetails().name()).isEqualTo("탄산2+1");
            assertThat(result.getDetails().bonusQuantity()).isEqualTo(1);
            assertThat(result.getDetails().purchaseQuantity()).isEqualTo(2);
        });
    }

}