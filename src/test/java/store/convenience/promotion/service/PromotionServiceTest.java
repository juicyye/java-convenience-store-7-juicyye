package store.convenience.promotion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.repository.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;

class PromotionServiceTest {

    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        PromotionRepository repository = PromotionRepositoryImpl.getInstance();
        promotionService = new PromotionService(repository);
    }

    @Test
    @DisplayName("createDto에 정확한 값이 들어오면 promotion을 생성한다")
    void create() throws Exception {
        // given
        String input = "탄산2+1,2,1,2024-01-01,2024-12-31";
        promotionService.create(input);

        // when
        Promotion result = promotionService.getPromotion("탄산2+1");

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