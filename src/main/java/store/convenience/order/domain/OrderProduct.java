package store.convenience.order.domain;

import store.convenience.product.domain.Product;

public class OrderProduct {

    private Product product;
    private int count;
    private int orderPrice;

    private OrderProduct() {
    }

    public static OrderProduct create(Product product, int orderPrice, int count) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.product = product;
        orderProduct.count = count;
        orderProduct.orderPrice = orderPrice;

        product.removeStock(count);
        return orderProduct;
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

}