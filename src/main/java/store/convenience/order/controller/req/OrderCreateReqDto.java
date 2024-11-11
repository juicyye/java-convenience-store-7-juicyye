package store.convenience.order.controller.req;

import store.convenience.product.domain.Item;

public record OrderCreateReqDto(
        Item item,
        int count
) {
}