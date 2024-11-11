package store.mock;

import java.time.LocalDateTime;
import store.convenience.order.service.port.LocalDateTimeHolder;

public class FakeLocalDateTimeHolder implements LocalDateTimeHolder {

    private LocalDateTime localDateTime;

    public FakeLocalDateTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }
}