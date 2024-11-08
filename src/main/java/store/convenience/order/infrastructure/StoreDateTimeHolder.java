package store.convenience.order.infrastructure;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.convenience.order.service.port.DateTimeHolder;

public class StoreDateTimeHolder implements DateTimeHolder {

    private static final DateTimeHolder instance = new StoreDateTimeHolder();

    private StoreDateTimeHolder() {
    }

    public static DateTimeHolder getInstance() {
        return instance;
    }

    @Override
    public LocalDateTime now() {
        return DateTimes.now();
    }

}