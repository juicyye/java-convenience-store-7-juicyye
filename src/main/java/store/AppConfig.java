package store;

import store.convenience.order.controller.OrderController;
import store.convenience.order.controller.input.InputHandler;
import store.convenience.order.controller.input.InputProcessor;
import store.convenience.order.controller.input.InputView;
import store.convenience.order.infrastructure.OrderRepositoryImpl;
import store.convenience.order.infrastructure.StoreDateTimeHolder;
import store.convenience.order.service.DiscountService;
import store.convenience.order.service.OrderAdjustmentService;
import store.convenience.order.service.OrderMessageService;
import store.convenience.order.service.OrderPromotionService;
import store.convenience.order.service.OrderService;
import store.convenience.order.service.port.DateTimeHolder;
import store.convenience.order.service.port.OrderRepository;
import store.convenience.product.controller.ProductController;
import store.convenience.product.infrastructure.ProductRepositoryImpl;
import store.convenience.product.service.ProductMessageService;
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

    public ProductController productController() {
        return new ProductController(productService());
    }

    public PromotionController promotionController() {
        return new PromotionController(promotionService());
    }

    public OrderController orderController() {
        return new OrderController(orderService(), inputView(), checkService(),
                orderAdjustmentService(), inputProcessor(), productMessageService(),
                orderMessageService());
    }

    /**
     * Service
     */

    private ProductService productService() {
        return new ProductService(productRepository(), promotionRepository());
    }

    private PromotionService promotionService() {
        return new PromotionService(promotionRepository());
    }

    private OrderService orderService() {
        return new OrderService(orderRepository(), productRepository(), discountService());
    }

    private OrderPromotionService checkService() {
        return new OrderPromotionService(productRepository());
    }

    private OrderAdjustmentService orderAdjustmentService() {
        return new OrderAdjustmentService();
    }

    private ProductMessageService productMessageService() {
        return new ProductMessageService(productService());
    }

    private DiscountService discountService() {
        return new DiscountService(checkService());
    }

    private OrderMessageService orderMessageService() {
        return new OrderMessageService();
    }

    /**
     * Repository
     */

    private ProductRepository productRepository() {
        return ProductRepositoryImpl.getInstance();
    }

    private PromotionRepository promotionRepository() {
        return PromotionRepositoryImpl.getInstance();
    }

    private OrderRepository orderRepository() {
        return OrderRepositoryImpl.getInstance();
    }

    /**
     * 기타
     */

    private DateTimeHolder dateTimeHolder() {
        return StoreDateTimeHolder.getInstance();
    }

    private InputHandler inputHandler() {
        return new InputHandler(dateTimeHolder());
    }

    private InputView inputView() {
        return new InputView(inputHandler());
    }

    private InputProcessor inputProcessor() {
        return new InputProcessor();
    }

}