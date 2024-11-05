package store.convenience.item.service.port;

import java.util.List;
import store.convenience.item.domain.Product;

public interface ItemRepository {

    void save(Product product);
    List<Product> findAll();


}
