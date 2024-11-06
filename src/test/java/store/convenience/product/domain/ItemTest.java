package store.convenience.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

class ItemTest {

    @Test
    @DisplayName("올바른 값을 입력하면 ITEM을 반환한다")
    void getItem() throws Exception {
        // given
        Item item = Item.of("콜라");

        // when then
        assertThat(item).isEqualByComparingTo(Item.COLA);
    }

    @Test
    @DisplayName("Item에 없는 값을 입력하면 에러를 반환한다")
    void withoutItem() throws Exception {
        //then
        assertThatThrownBy(() -> Item.of("아무거나"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

}