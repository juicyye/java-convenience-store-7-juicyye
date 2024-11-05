package store.convenience.product.controller;

import java.util.List;
import store.convenience.product.service.ProductService;
import store.global.util.Reader;
import store.global.util.StoreConstant;

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void start(){
        List<String> productData = Reader.readFiles(StoreConstant.PRODUCT_PATH);
        productData.forEach(productService::register);
    }
}
