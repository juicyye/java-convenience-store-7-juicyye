package store.convenience.product.domain;

import java.util.Collections;
import java.util.List;

public class ProductInventory {

    private List<Product> products;

    public ProductInventory(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

}