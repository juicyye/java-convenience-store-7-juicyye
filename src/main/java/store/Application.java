package store;

import store.convenience.product.controller.ProductController;
import store.convenience.promotion.controller.PromotionController;

public class Application {

    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();

        PromotionController promotionController = appConfig.promotionController();
        promotionController.start();

        ProductController productController = appConfig.productController();
        productController.start();
    }

}
