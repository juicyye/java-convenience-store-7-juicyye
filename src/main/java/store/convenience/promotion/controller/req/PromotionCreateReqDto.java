package store.convenience.promotion.controller.req;

public record PromotionCreateReqDto(
        String name,
        int buy,
        int get,
        String startDate,
        String endDate
) {

}
