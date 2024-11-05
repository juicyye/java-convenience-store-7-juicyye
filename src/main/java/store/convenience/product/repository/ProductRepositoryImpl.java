package store.convenience.product.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {

    private final Map<String, List<Product>> products = new HashMap<>();

    @Override
    public void save(Product product) {
        String itemName = product.getItem().getName();
        List<Product> productList = products.getOrDefault(itemName, new ArrayList<>());

        productList.add(product);
        products.put(itemName, productList);
    }

    @Override
    public List<List<Product>> findAll() {
        return products.values().stream().toList();
    }

    @Override
    public List<Product> findByName(String productName) {
        return products.get(productName);
    }

    @Override
    public void clear() {
        products.clear();
    }
}
