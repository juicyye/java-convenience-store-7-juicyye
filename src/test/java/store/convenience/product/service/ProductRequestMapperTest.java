package store.convenience.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;

class ProductRequestMapperTest {

    private ProductRequestMapper mapper = ProductRequestMapper.getInstance();

    @Test
    @DisplayName("String[]으로 입력하면 Product가 생성된다")
    void createStringArrayMapper() throws Exception {
        // given
        String[] input = {"콜라", "1000", "10", "탄산2+1"};
        Promotion promotion = createPromotion();

        // when
        Product product = mapper.create(input, promotion);

        // then
        assertAll(() -> {
            assertThat(product.getItem()).isEqualByComparingTo(Item.COLA);
            assertThat(product.getQuantity()).isEqualTo(10);
            assertThat(product.getApplicablePromotion().get()).isEqualTo(promotion);
        });
    }

    @Test
    @DisplayName("Item, quantity 입력하면 Product가 생성된다")
    void createMapper() throws Exception {
        // given
        Item item = Item.ORANGE_JUICE;
        int quantity = 10;

        // when
        Product product = mapper.create(item, quantity, null);

        // then
        assertAll(() -> {
            assertThat(product.getItem()).isEqualByComparingTo(item);
            assertThat(product.getQuantity()).isEqualTo(quantity);
            assertThat(product.getApplicablePromotion()).isEmpty();
        });
    }

    private Promotion createPromotion() {
        return new Promotion(new PromotionDetails("name", 3, 2),
                LocalDate.now(), LocalDate.now());
    }

}