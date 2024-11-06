package store.convenience.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static store.global.util.StoreConstant.MAX_MEMBERSHIP_DISCOUNT;
import static store.global.util.StoreConstant.MEMBERSHIP_RATE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.convenience.order.domain.Discount;
import store.convenience.order.domain.PromotionCheck;
import store.convenience.product.domain.Item;

class OrderPromotionServiceTest {

    private final OrderPromotionService orderPromotionService = new OrderPromotionService();

    @Test
    @DisplayName("주문에 대한 할인이 적용된다")
    void OrderDiscount() throws Exception {
        // given
        PromotionCheck promotionCheck = new PromotionCheck(Item.COLA, 3, true, true, 3, 4);

        // when
        Discount discount = orderPromotionService.calculateOrderDiscount(List.of(promotionCheck), true);

        // then
        assertThat(discount).isNotNull()
                .extracting(Discount::getMembershipDiscount, Discount::getTotalPrice, Discount::getPromotionDiscount)
                .containsExactlyInAnyOrder(3000, 3000,
                        Math.min(MAX_MEMBERSHIP_DISCOUNT, (int) (3000 * MEMBERSHIP_RATE)))
        ;
    }

}