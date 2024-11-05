package store.convenience.product.service.port;

import java.util.List;
import store.convenience.product.domain.Product;

public interface ProductRepository {

    void save(Product product);

    List<List<Product>> findAll();

    List<Product> findByName(String productName);

    void clear();

}
