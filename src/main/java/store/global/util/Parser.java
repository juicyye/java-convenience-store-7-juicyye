package store.global.util;

import java.util.Arrays;

public abstract class Parser {

    public static int convertToInt(String input) {
        try{
            return Integer.parseInt(input);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage(), e);
        }
    }

    public static String[] splitByComma(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }

        return Arrays.stream(input.split(StoreConstant.DELIMITER))
                .map(String::trim).toArray(String[]::new);
    }

}
