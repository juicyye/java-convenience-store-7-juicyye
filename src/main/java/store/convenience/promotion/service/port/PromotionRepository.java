package store.convenience.promotion.service.port;

import java.util.Optional;
import store.convenience.promotion.domain.Promotion;

public interface PromotionRepository {

    void save(Promotion promotion);

    Optional<Promotion> findByName(String name);

    void clear();

}