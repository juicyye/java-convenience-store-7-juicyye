package store.global.util;

import static store.global.util.StoreConstant.DELIMITER_COMMA;
import static store.global.util.StoreConstant.DELIMITER_DASH;

import java.util.Arrays;

public abstract class Parser {

    public static int convertToInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage(), e);
        }
    }

    public static String[] splitByComma(String input) {
        validateEmpty(input);
        return Arrays.stream(input.split(DELIMITER_COMMA))
                .map(String::trim).toArray(String[]::new);
    }

    public static String[] splitByDash(String input) {
        validateEmpty(input);
        return Arrays.stream(input.split(DELIMITER_DASH))
                .map(String::trim).toArray(String[]::new);
    }

    private static void validateEmpty(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }

}