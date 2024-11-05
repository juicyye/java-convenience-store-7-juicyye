package store.convenience.item.domain;

import java.util.List;

public class ProductInventory {

    private List<Product> products;

    public ProductInventory(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

}
