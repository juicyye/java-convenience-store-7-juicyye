package store.convenience.product.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;

class ProductRepositoryImplTest {

    private ProductRepository productRepository = ProductRepositoryImpl.getInstance();

    @AfterEach
    void tearDown() {
        productRepository.clear();
    }

    @Nested
    @DisplayName("product를 생성하고 조회할 수 있다")
    class createProduct {
        Item item = Item.COLA;
        Item noPromotion = Item.ORANGE_JUICE;
        int quantity = 10;


        @BeforeEach
        void setUp() {
            Product product1 = new Product(item, quantity, createPromotion());
            Product product2 = new Product(item, quantity, null);
            Product product3 = new Product(noPromotion, quantity, null);
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
        }

        @Test
        @DisplayName("프로모션이 있는 아이템을 먼저 조회한다")
        void findFirstPromotion() throws Exception {
            // when
            Product result = productRepository.findByName(item.getName()).orElse(null);

            // then
            assertAll(() -> {
                assertThat(result.getItem()).isEqualByComparingTo(item);
                assertThat(result.getApplicablePromotion()).isPresent();
                assertThat(result.getQuantity()).isEqualTo(quantity);
            });
        }

        @Test
        @DisplayName("프로모션이 없으면 일반 아이템을 조회한다")
        void findNoPromotionIfNoPromotion() throws Exception {
            // when
            Product result = productRepository.findByName(noPromotion.getName()).orElse(null);

            // then
            assertAll(() -> {
                assertThat(result.getItem()).isEqualByComparingTo(noPromotion);
                assertThat(result.getApplicablePromotion()).isEmpty();
                assertThat(result.getQuantity()).isEqualTo(quantity);
            });
        }

        @Test
        @DisplayName("프로모션이 없는 아이템을 지정해서 조회할 수 있다")
        void findNoPromotion() throws Exception {
            // when
            Product result = productRepository.findByNameAndNoPromotion(item.getName()).orElse(null);

            // then
            assertAll(() -> {
                assertThat(result.getItem()).isEqualByComparingTo(item);
                assertThat(result.getApplicablePromotion()).isEmpty();
                assertThat(result.getQuantity()).isEqualTo(quantity);
            });
        }
    }

    private Promotion createPromotion() {
        return new Promotion(new PromotionDetails("name", 3, 2),
                LocalDate.now(), LocalDate.now());
    }

}