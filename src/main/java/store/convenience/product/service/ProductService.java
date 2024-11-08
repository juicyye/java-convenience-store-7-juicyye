package store.convenience.product.service;

import static store.global.util.StoreConstant.PROMOTION_INDEX;

import java.util.ArrayList;
import java.util.List;
import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.product.domain.ProductInventory;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.service.port.PromotionRepository;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;
import store.global.util.Parser;

public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductRequestMapper mapper;

    public ProductService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.mapper = ProductRequestMapper.getInstance();
    }

    public void register(String productInput) {
        String[] productParts = Parser.splitByComma(productInput);
        Promotion promotion = retrievePromotion(productParts[PROMOTION_INDEX]);
        Product product = mapper.create(productParts, promotion);
        productRepository.save(product);
    }

    private Promotion retrievePromotion(String promotionName) {
        return promotionRepository.findByName(promotionName)
                .orElse(null);
    }

    public Product getNonPromotedProduct(String itemName) {
        return productRepository.findByName(itemName)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    public List<ProductInventory> findAll() {
        List<ProductInventory> inventory = new ArrayList<>();
        productRepository.findAll().forEach(
                p -> inventory.add(new ProductInventory(p))
        );
        return inventory;
    }

    public void addNonPromotionItems() {
        List<List<Product>> products = productRepository.findAll();
        for (List<Product> product : products) {
            boolean hasPromotion = product.stream().anyMatch(item -> item.getApplicablePromotion().isPresent());
            boolean hasNormal = product.stream().anyMatch(item -> item.getApplicablePromotion().isEmpty());

            if (hasPromotion && !hasNormal) {
                Item item = product.getFirst().getItem();
                productRepository.save(mapper.create(item, 0, null));
            }
        }
    }

}