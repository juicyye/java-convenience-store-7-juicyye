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

    private static final ProductRepository instance = new ProductRepositoryImpl();

    private ProductRepositoryImpl() {
    }

    public static ProductRepository getInstance() {
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

    public Optional<Product> findByName(String productName) {
        return products.get(productName).stream()
                .filter(i -> i.getApplicablePromotion().isPresent())
                .findFirst()
                .or(() -> products.get(productName).stream()
                        .filter(i -> i.getApplicablePromotion().isEmpty())
                        .findFirst());
    }

    @Override
    public Optional<Product> findByNameAndNoPromotion(String productName) {
        return products.get(productName).stream()
                .filter(i -> i.getApplicablePromotion().isEmpty())
                .findFirst();
    }

    @Override
    public void clear() {
        products.clear();
    }

}