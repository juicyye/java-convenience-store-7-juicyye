package store.convenience.product.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.convenience.product.domain.Product;
import store.convenience.product.service.port.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {

    private final Map<String, List<Product>> products = new HashMap<>();

    private static final ProductRepositoryImpl instance = new ProductRepositoryImpl();

    private ProductRepositoryImpl() {
    }

    public static ProductRepositoryImpl getInstance() {
        return instance;
    }

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
    public Optional<Product> findByNameAndPromotion(String productName) {
        return products.get(productName).stream()
                .filter(i -> i.getPromotion() != null)
                .findFirst();
    }

    @Override
    public Optional<Product> findByNameAndNoPromotion(String productName) {
        return products.get(productName).stream()
                .filter(i -> i.getPromotion() == null)
                .findFirst();
    }

    @Override
    public void clear() {
        products.clear();
    }

}
