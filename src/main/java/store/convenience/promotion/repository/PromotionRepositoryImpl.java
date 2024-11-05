package store.convenience.promotion.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.service.port.PromotionRepository;

public class PromotionRepositoryImpl implements PromotionRepository {

    private final List<Promotion> promotions = new ArrayList<>();

    @Override
    public void save(Promotion promotion) {
        promotions.add(promotion);
    }

    @Override
    public Optional<Promotion> findByName(String name) {
        return promotions.stream().filter(p -> p.getDetails().name().equals(name)).findFirst();
    }

}
