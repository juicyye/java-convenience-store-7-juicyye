package store.global.util;

public enum ErrorMessage {

    INVALID_END_DATE("종료 날짜는 시작 날짜 이후여야 합니다."),
    INVALID_DATE_FORMAT("잘못된 날짜 형식입니다."),

    NOT_FOUND_PROMOTION("해당 프로모션을 찾을 수 없습니다.")
    ;

    private final String PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
