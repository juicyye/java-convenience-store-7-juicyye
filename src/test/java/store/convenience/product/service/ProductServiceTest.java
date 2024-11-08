package store.convenience.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.domain.ProductInventory;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.port.PromotionRepository;

class ProductServiceTest {

    private final ProductRepository productRepository = ProductRepositoryImpl.getInstance();
    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private final ProductService productService = new ProductService(productRepository, promotionRepository);

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion(getDetails(), getDate(), getDate());
        promotionRepository.save(promotion);
    }

    @AfterEach
    void after() {
        productRepository.clear();
        promotionRepository.clear();
    }

    @ParameterizedTest
    @DisplayName("정확한 입력이 주어지면 Product가 정상적으로 저장 및 조회가 된다")
    @MethodSource("providedProductCreate")
    void saveProduct(String input, String itemName, int quantity, Item expectItem, boolean hasPromotion) throws Exception {
        // given
        productService.register(input);

        // when
        Product result = productRepository.findByName(itemName).orElse(null);

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getQuantity()).isEqualTo(quantity);
            assertThat(result.getPromotion() != null).isEqualTo(hasPromotion);
            assertThat(result.getItem()).isEqualByComparingTo(expectItem);
        });
    }

    private static Stream<Arguments> providedProductCreate() {
        return Stream.of(
                Arguments.arguments("콜라,1000,10,탄산2+1", "콜라", 10, Item.COLA, true),
                Arguments.arguments("사이다,1000,10,null", "사이다", 10, Item.CIDER, false)
        );
    }

    @Test
    @DisplayName("findAll 메서드로 값을 호출하여 일급 객체로 감싼다")
    void findAll() throws Exception {
        // given
        String cola = "콜라,1000,10,탄산2+1";
        String cider = "사이다,1000,8,탄산2+1";
        productService.register(cola);
        productService.register(cider);

        // when
        List<ProductInventory> results = productService.findAll();

        // then
        assertThat(results).hasSize(2)
                .flatExtracting(ProductInventory::getProducts)
                .hasSize(2)
                .extracting(Product::getItem, Product::getQuantity)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(Item.COLA, 10),
                        Tuple.tuple(Item.CIDER, 8)
                );
    }

    @Test
    @DisplayName("프로모션이 있는 아이템만 생성할 경우 프로모션이 없는 아이템도 생성해준다")
    void addNonPromotionItems() throws Exception {
        // given
        String cola = "콜라,1000,10,탄산2+1";
        productService.register(cola);
        productService.addNonPromotionItems();

        // when
        Product result = productService.getNonPromotedProduct("콜라");

        // then
        assertThat(result).isNotNull();
    }

    private LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails() {
        return new PromotionDetails("탄산2+1", 2, 3);
    }

}