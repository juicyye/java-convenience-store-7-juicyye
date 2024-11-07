package store.convenience.receipt.service.port;

import java.util.List;
import store.convenience.receipt.domain.Receipt;

public interface ReceiptRepository {

    void save(Receipt receipt);

    List<Receipt> findAll();

}
