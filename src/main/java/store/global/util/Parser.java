package store.global.util;

public abstract class Parser {

    public static int convertToInt(String input) {
        try{
            return Integer.parseInt(input);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage(), e);
        }
    }

}
