package store.convenience.item.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.item.controller.req.ProductCreateReqDto;
import store.convenience.item.domain.Item;
import store.convenience.item.domain.Product;
import store.convenience.item.domain.ProductInventory;
import store.convenience.item.repository.ProductRepositoryImpl;
import store.convenience.item.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;

class ProductServiceTest {

    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final ProductService productService = new ProductService(productRepository);

    @BeforeEach
    void setUp() {
        productRepository.clear();
    }

    @Test
    @DisplayName("CreateDto와 Promotion으로 Product를 생성한다")
    void create() throws Exception {
        // given
        ProductCreateReqDto dto = getProductCreateReqDto("콜라", 3);
        Promotion promotion = new Promotion(getDetails(), getDate(), getDate());
        productService.register(dto, promotion);

        // when
        List<Product> result = productService.getProducts("콜라");

        // then
        assertThat(result).hasSize(1)
                .allSatisfy(p -> {
                            assertThat(p.getPromotion()).isEqualTo(promotion);
                            assertThat(p.getQuantity()).isEqualTo(3);
                            assertThat(p.getItem().getName()).isEqualTo("콜라");
                        }
                );
    }

    @Test
    @DisplayName("findAll 메서드로 값을 호출하여 일급 객체로 감싼다")
    void findAll() throws Exception {
        // given
        ProductCreateReqDto cola = getProductCreateReqDto("콜라", 3);
        ProductCreateReqDto cider = getProductCreateReqDto("사이다", 3);
        Promotion promotion = new Promotion(getDetails(), getDate(), getDate());
        productService.register(cola, promotion);
        productService.register(cider, promotion);

        // when
        List<ProductInventory> results = productService.findAll();

        // then
        assertThat(results).hasSize(2)
                .flatExtracting(ProductInventory::getProducts)
                .hasSize(2)
                .allSatisfy(p -> {
                    assertThat(p.getPromotion()).isEqualTo(promotion);
                    assertThat(p.getQuantity()).isEqualTo(3);
                })
                .anySatisfy(p -> {
                    assertThat(p.getItem()).isEqualTo(Item.COLA);
                })
                .anySatisfy(p -> {
                    assertThat(p.getItem()).isEqualTo(Item.CIDER);
                });
    }

    @Test
    @DisplayName("프로모션이 있는 아이템만 생성할 경우 프로모션이 없는 아이템도 생성해준다")
    void addNonPromotionItems() throws Exception {
        // given
        ProductCreateReqDto cola = getProductCreateReqDto("콜라", 3);
        Promotion promotion = new Promotion(getDetails(), getDate(), getDate());
        productService.register(cola, promotion);
        productService.addNonPromotionItems();

        // when
        List<Product> result = productService.getProducts("콜라");

        // then
        assertThat(result).hasSize(2)
                .allSatisfy(p -> {
                    assertThat(p.getItem()).isEqualTo(Item.COLA);
                });
    }

    private ProductCreateReqDto getProductCreateReqDto(String name, int quantity) {
        return new ProductCreateReqDto(name, quantity);
    }

    private LocalDate getDate() {
        return LocalDate.of(2024, 5, 5);
    }

    private PromotionDetails getDetails() {
        return new PromotionDetails("detail", 2, 3);
    }

}