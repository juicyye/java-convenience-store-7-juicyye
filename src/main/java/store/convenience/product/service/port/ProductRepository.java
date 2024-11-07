package store.convenience.product.service.port;

import java.util.List;
import java.util.Optional;
import store.convenience.product.domain.Product;

public interface ProductRepository {

    void save(Product product);

    List<List<Product>> findAll();

    Optional<Product> findByName(String productName);

    Optional<Product> findByNameAndNoPromotion(String productName);

    void clear();

}
