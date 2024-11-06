package store;

import store.convenience.product.controller.ProductController;
import store.convenience.product.controller.ProductMessageFormatter;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.ProductService;
import store.convenience.product.service.port.ProductRepository;
import store.convenience.promotion.controller.PromotionController;
import store.convenience.promotion.infrastrcuture.PromotionRepositoryImpl;
import store.convenience.promotion.service.PromotionService;
import store.convenience.promotion.service.port.PromotionRepository;

public class AppConfig {

    /**
     * Controller
     */

    public ProductController productController(){
        return new ProductController(productService(), productMessageFormatter());
    }

    public PromotionController promotionController(){
        return new PromotionController(promotionService());
    }

    private ProductMessageFormatter productMessageFormatter(){
        return new ProductMessageFormatter();
    }

    /**
     * Service
     */

    private ProductService productService(){
        return new ProductService(productRepository(),promotionRepository());
    }

    private PromotionService promotionService(){
        return new PromotionService(promotionRepository());
    }

    /**
     * Repository
     */

    private ProductRepository productRepository(){
        return ProductRepositoryImpl.getInstance();
    }

    private PromotionRepository promotionRepository(){
        return PromotionRepositoryImpl.getInstance();
    }

}
