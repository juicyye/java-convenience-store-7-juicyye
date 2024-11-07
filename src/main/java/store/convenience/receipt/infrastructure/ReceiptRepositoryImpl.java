package store.convenience.receipt.infrastructure;

import java.util.ArrayList;
import java.util.List;
import store.convenience.receipt.domain.Receipt;
import store.convenience.receipt.service.port.ReceiptRepository;

public class ReceiptRepositoryImpl implements ReceiptRepository {
    private final List<Receipt> receipts = new ArrayList<>();
    private static final ReceiptRepository instance = new ReceiptRepositoryImpl();

    public static ReceiptRepository getInstance() {
        return instance;
    }

    private ReceiptRepositoryImpl() {
    }

    @Override
    public void save(Receipt receipt) {
        receipts.add(receipt);
    }

    @Override
    public List<Receipt> findAll() {
        return receipts;
    }
}

