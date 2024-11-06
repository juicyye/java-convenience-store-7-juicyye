package store.convenience.order.infrastructure;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.convenience.order.service.port.DateTimeHolder;

public class StoreDateTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime now() {
        return DateTimes.now();
    }

}
