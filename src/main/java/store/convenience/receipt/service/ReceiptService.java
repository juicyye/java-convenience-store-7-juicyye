package store.convenience.receipt.service;

import java.util.List;
import store.convenience.receipt.domain.Receipt;
import store.convenience.receipt.service.port.ReceiptRepository;

public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public void create() {

    }

    public List<Receipt> getAllReceipts(){
        return receiptRepository.findAll();
    }

}
