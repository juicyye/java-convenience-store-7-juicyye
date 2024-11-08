package store.convenience.product.service;

import java.util.List;
import store.convenience.product.domain.ProductInventory;

public class ProductMessageService {

    private final ProductMessageFormatter messageFormatter;
    private final ProductService productService;

    public ProductMessageService(ProductMessageFormatter messageFormatter,
                                 ProductService productService) {
        this.messageFormatter = messageFormatter;
        this.productService = productService;
    }

    public String showProductInventory() {
        List<ProductInventory> inventories = productService.findAll();
        return messageFormatter.formatInventory(inventories);
    }

}