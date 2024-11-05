package store.convenience.promotion.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import store.convenience.promotion.controller.req.PromotionCreateReqDto;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.global.util.ErrorMessage;

public class PromotionRequestMapper {

    private static final PromotionRequestMapper instance = new PromotionRequestMapper();

    private PromotionRequestMapper() {
    }

    public static PromotionRequestMapper getInstance() {
        return instance;
    }

    public Promotion create(PromotionCreateReqDto createReqDto) {

        return new Promotion(
                createPromotionDetails(createReqDto),
                parseDate(createReqDto.startDate()),
                parseDate(createReqDto.endDate())
        );
    }

    private PromotionDetails createPromotionDetails(PromotionCreateReqDto createReqDto) {
        return new PromotionDetails(createReqDto.name(), createReqDto.buy(), createReqDto.get());
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
        }
    }

}
