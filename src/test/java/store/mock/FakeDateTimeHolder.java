package store.mock;

import java.time.LocalDateTime;
import store.convenience.order.service.port.DateTimeHolder;

public class FakeDateTimeHolder implements DateTimeHolder {

    private LocalDateTime localDateTime;

    public FakeDateTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }
}