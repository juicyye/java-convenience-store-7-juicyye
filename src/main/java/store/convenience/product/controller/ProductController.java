package store.convenience.product.controller;

import java.util.List;
import store.convenience.product.service.ProductMessageFormatter;
import store.convenience.product.service.ProductService;
import store.global.util.Reader;
import store.global.util.StoreConstant;

public class ProductController {

    private final ProductService productService;
    private final ProductMessageFormatter messageFormatter;

    public ProductController(ProductService productService, ProductMessageFormatter messageFormatter) {
        this.productService = productService;
        this.messageFormatter = messageFormatter;
    }

    public void start() {
        processProductData();
    }

    private void processProductData() {
        List<String> productData = Reader.readFiles(StoreConstant.PRODUCT_PATH);
        productData.forEach(productService::register);
        productService.addNonPromotionItems();
    }

}