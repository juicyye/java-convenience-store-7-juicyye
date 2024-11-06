package store.convenience.order.domain;

import java.util.ArrayList;
import java.util.List;
import store.convenience.product.domain.Product;

public class Order {

    private List<Product> products = new ArrayList<>();
    private int count;
    private int discountPrice;

    public Order(List<Product> products, int count, int discountPrice) {
        this.products = products;
        this.count = count;
        this.discountPrice = discountPrice;
    }

}
