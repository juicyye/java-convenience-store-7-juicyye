package store.convenience.promotion.service;

import static store.global.util.StoreConstant.STANDARD_DATE_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import store.convenience.promotion.domain.Promotion;
import store.convenience.promotion.domain.PromotionDetails;
import store.global.exception.ErrorMessage;
import store.global.util.Parser;

public class PromotionRequestMapper {

    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    private static final PromotionRequestMapper instance = new PromotionRequestMapper();

    private PromotionRequestMapper() {
    }

    public static PromotionRequestMapper getInstance() {
        return instance;
    }


    public Promotion create(String[] promotionParts) {
        return new Promotion(
                createPromotionDetails(promotionParts[NAME_INDEX], promotionParts[BUY_INDEX],
                        promotionParts[GET_INDEX]),
                parseDate(promotionParts[START_DATE_INDEX]),
                parseDate(promotionParts[END_DATE_INDEX])
        );
    }

    private PromotionDetails createPromotionDetails(String name, String buy, String get) {
        return new PromotionDetails(name, Parser.convertToInt(buy), Parser.convertToInt(get));
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT);
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
        }
    }

}