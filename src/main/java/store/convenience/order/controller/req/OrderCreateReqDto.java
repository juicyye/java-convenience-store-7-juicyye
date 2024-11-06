package store.convenience.order.controller.req;

public record OrderCreateReqDto(
        String itemName,
        int count
) {
}
