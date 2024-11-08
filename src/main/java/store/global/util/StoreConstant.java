package store.global.util;

public abstract class StoreConstant {

    public static final String PROMOTION_PATH = "src/main/resources/promotions.md";
    public static final String PRODUCT_PATH = "src/main/resources/products.md";
    public static final String DELIMITER_COMMA = ",";
    public static final String DELIMITER_DASH = "-";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";

    public static final int PROMOTION_INDEX = 3;
    public static final int NAME_INDEX = 0;
    public static final int QUANTITY_INDEX = 2;
    public static final int COUNT_INDEX = 1;

    public static final String ITEM_PATTERN = "\\[([가-힣]+)-([0-9]+)\\]";
    public static final String BRACKET_REGEX = "[\\[\\]]";

    public static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    public static final double MEMBERSHIP_RATE = 0.3;

    public static final int DEFAULT_QUANTITY = 0;
    public static final int STANDARD_NUMBER = 0;
    public static final int DISCOUNT_MULTIPLIER = 100;

}