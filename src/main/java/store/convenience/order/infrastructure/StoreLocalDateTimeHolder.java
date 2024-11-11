package store.convenience.order.infrastructure;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.convenience.order.service.port.LocalDateTimeHolder;

public class StoreLocalDateTimeHolder implements LocalDateTimeHolder {

    private static final LocalDateTimeHolder instance = new StoreLocalDateTimeHolder();

    private StoreLocalDateTimeHolder() {
    }

    public static LocalDateTimeHolder getInstance() {
        return instance;
    }

    @Override
    public LocalDateTime now() {
        return DateTimes.now();
    }

}