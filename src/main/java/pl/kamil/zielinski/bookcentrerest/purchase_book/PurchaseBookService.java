package pl.kamil.zielinski.bookcentrerest.purchase_book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;

@Service
public class PurchaseBookService {
    private PurchaseBookRepository purchaseBookRepository;

    @Autowired
    PurchaseBookService(PurchaseBookRepository purchaseBookRepository) {
        this.purchaseBookRepository = purchaseBookRepository;
    }

    public void addPurchaseBook(PurchaseBookEntity purchaseBookEntity) throws RequestException {
        try {
            purchaseBookRepository.save(purchaseBookEntity);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try Again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void removePurchaseBook(PurchaseBookEntity purchaseBookEntity) throws RequestException {
        try {
            purchaseBookRepository.delete(purchaseBookEntity);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try Again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
