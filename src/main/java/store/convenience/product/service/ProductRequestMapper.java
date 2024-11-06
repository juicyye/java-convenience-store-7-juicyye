package store.convenience.product.service;

import static store.global.util.StoreConstant.NAME_INDEX;
import static store.global.util.StoreConstant.QUANTITY_INDEX;

import store.convenience.product.domain.Item;
import store.convenience.product.domain.Product;
import store.convenience.promotion.domain.Promotion;
import store.global.util.Parser;

public class ProductRequestMapper {

    private static final ProductRequestMapper instance = new ProductRequestMapper();

    private ProductRequestMapper() {
    }

    public static ProductRequestMapper getInstance() {
        return instance;
    }

    public Product create(String[] productParts, Promotion promotion) {
        Item item = Item.of(productParts[NAME_INDEX]);
        return new Product(item, Parser.convertToInt(productParts[QUANTITY_INDEX]), promotion);
    }

    public Product create(Item item, int quantity, Promotion promotion) {
        return new Product(item, quantity, promotion);
    }

}
