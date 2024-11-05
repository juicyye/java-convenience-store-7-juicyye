package store.convenience.product.service;

import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.promotion.domain.Promotion;

public class ProductRequestMapper {

    private static final ProductRequestMapper instance = new ProductRequestMapper();

    private ProductRequestMapper() {
    }

    public static ProductRequestMapper getInstance() {
        return instance;
    }

    public Product create(String itemName, int quantity, Promotion promotion) {
        Item item = Item.of(itemName);
        return new Product(item, quantity, promotion);
    }

}
