package store.global.util;

public enum ErrorMessage {

    INVALID_END_DATE("종료 날짜는 시작 날짜 이후여야 합니다."),
    INVALID_DATE_FORMAT("잘못된 날짜 형식입니다."),
    ERROR_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),

    NOT_FOUND_PROMOTION("해당 프로모션을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),

    ERROR_READING_FILE("파일을 읽는 도중 오류가 발생했습니다. 확인 후 다시 시도해주세요"),

    INVALID_NUMBER_FORMAT("숫자를 입력해주세요"),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요"),

    OUT_OF_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private final String PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
