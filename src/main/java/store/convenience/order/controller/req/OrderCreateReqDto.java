package store.convenience.order.controller.req;

import java.time.LocalDate;

public record OrderCreateReqDto(
        String itemName,
        int count,
        LocalDate currentDate
) {
}