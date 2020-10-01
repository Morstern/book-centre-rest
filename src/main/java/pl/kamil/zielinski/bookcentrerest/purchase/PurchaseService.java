package pl.kamil.zielinski.bookcentrerest.purchase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.kamil.zielinski.bookcentrerest.book.BookService;
import pl.kamil.zielinski.bookcentrerest.commons.HeaderBuilder;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;
import pl.kamil.zielinski.bookcentrerest.purchase_book.PurchaseBookEntity;
import pl.kamil.zielinski.bookcentrerest.purchase_book.PurchaseBookService;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class PurchaseService {
    private PurchaseRepository purchaseRepository;
    private PurchaseBookService purchaseBookService;
    private BookService bookService;

    @Autowired
    PurchaseService(PurchaseRepository purchaseRepository, PurchaseBookService purchaseBookService, BookService bookService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseBookService = purchaseBookService;
        this.bookService = bookService;
    }


    public int getAmountOfPurchases() {
        return ((Collection<?>) purchaseRepository.findAll()).size();
    }

    public ResponseEntity<?> createPurchase() throws RequestException {
        try {
            PurchaseEntity newPurchaseEntity = new PurchaseEntity();
            PurchaseEntity createdEntity = purchaseRepository.save(newPurchaseEntity);
            MultiValueMap<String, String> headers = new HeaderBuilder()
                    .setETag(String.valueOf(createdEntity.hashCode()))
                    .setLocation(UriConstants.purchaseUri+"/" + createdEntity.getPurchaseId())
                    .build();

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updatePurchase(int id, int hashcode, PurchaseEntity updatePurchaseEntity) throws RequestException {
        Optional<PurchaseEntity> optionalPurchaseEntity = purchaseRepository.findById(id);
        if (optionalPurchaseEntity.isEmpty()) {
            throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalPurchaseEntity.get().hashCode() != hashcode) {
            throw new RequestException("There has been a new update for the purchase resource with id: " + optionalPurchaseEntity.get().getPurchaseId() + ", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }
        try {
            PurchaseEntity databasePurchaseEntity = optionalPurchaseEntity.get();
            Collection<PurchaseBookEntity> purchaseBookEntities = updatePurchaseEntity.getPurchasedBooks();
            for (PurchaseBookEntity purchaseBookEntity : purchaseBookEntities) {
                bookService.subtractAmountFromBookAmount(purchaseBookEntity.getBookId(), purchaseBookEntity.getAmount());
                purchaseBookEntity.setPurchaseId(updatePurchaseEntity.getPurchaseId());
                purchaseBookService.addPurchaseBook(purchaseBookEntity);
            }
            databasePurchaseEntity.setClientId(updatePurchaseEntity.getClientId());
            databasePurchaseEntity.setIsPaid(updatePurchaseEntity.getIsPaid());
            databasePurchaseEntity.setOrderDate(updatePurchaseEntity.getOrderDate());
            databasePurchaseEntity.setPurchasedBooks(updatePurchaseEntity.getPurchasedBooks());
            return new ResponseEntity<>(purchaseRepository.save(databasePurchaseEntity), HttpStatus.OK);
        } catch (RequestException re) {
            throw re;
        }
    }

    public ResponseEntity<?> getPurchaseInfo(int id) throws RequestException {
        Optional<PurchaseEntity> optionalPurchaseEntity = purchaseRepository.findById(id);
        if (optionalPurchaseEntity.isEmpty()) {
            throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
        }

        PurchaseEntity purchaseEntity = optionalPurchaseEntity.get();
        MultiValueMap<String, String> headers = new HeaderBuilder()
                .setETag(String.valueOf(purchaseEntity.hashCode()))
                .build();
        try {
            return new ResponseEntity<>(purchaseEntity, headers, HttpStatus.OK);
        } catch (Exception e) {

            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getIsPaid(int id) throws RequestException {
        Optional<PurchaseEntity> optionalPurchaseEntity = purchaseRepository.findById(id);
        if (optionalPurchaseEntity.isEmpty()) {
            throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalPurchaseEntity.get().getIsPaid(), HttpStatus.OK);
    }

    public ResponseEntity<?> changeIsPaidValue(int id, int hashcode, int isPaid) throws RequestException {
        if (isPaid != 0 && isPaid != 1) {
            throw new RequestException("Provided new value is incorrect", HttpStatus.BAD_REQUEST);
        }

        Optional<PurchaseEntity> optionalPurchaseEntity = purchaseRepository.findById(id);
        if (optionalPurchaseEntity.isEmpty()) {
            throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalPurchaseEntity.get().hashCode() != hashcode) {
            throw new RequestException("There has been a new update for the purchase resource with id: " + optionalPurchaseEntity.get().getPurchaseId() + ", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        try {
            PurchaseEntity purchaseEntity = optionalPurchaseEntity.get();
            purchaseEntity.setIsPaid(isPaid);
            return new ResponseEntity<>(purchaseRepository.save(purchaseEntity), HttpStatus.OK);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> cancelPurchase(int id, int hashcode) throws RequestException {
        Optional<PurchaseEntity> optionalPurchaseEntity = purchaseRepository.findById(id);
        if (optionalPurchaseEntity.isEmpty()) {
            throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalPurchaseEntity.get().hashCode() != hashcode) {
            throw new RequestException("There has been a new update for the purchase resource with id: " + optionalPurchaseEntity.get().getPurchaseId() + ", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        if (optionalPurchaseEntity.get().getIsPaid() == 1) {
            throw new RequestException("Can not delete paid purchase", HttpStatus.BAD_REQUEST);
        }

        try {
            PurchaseEntity canceledPurchaseEntity = optionalPurchaseEntity.get();
            Collection<PurchaseBookEntity> purchaseBookEntities = canceledPurchaseEntity.getPurchasedBooks();

            for (PurchaseBookEntity purchaseBookEntity : purchaseBookEntities) {
                bookService.addAmountToBookAmount(purchaseBookEntity.getBookId(), purchaseBookEntity.getAmount());
                purchaseBookService.removePurchaseBook(purchaseBookEntity);
            }
            purchaseRepository.delete(canceledPurchaseEntity);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Transactional
    public ResponseEntity<?> mergePurchases(Collection<PurchaseEntity> purchaseEntityCollection) throws RequestException {
        try {
            PurchaseEntity newPurchaseEntity = purchaseRepository.save(new PurchaseEntity());
            newPurchaseEntity.setIsPaid(0);
            newPurchaseEntity.setOrderDate(new Date(Calendar.getInstance().getTime().getTime()));
            for (PurchaseEntity purchaseEntity : purchaseEntityCollection) {
                newPurchaseEntity.setClientId(purchaseEntity.getClientId());
                Optional<PurchaseEntity> foundPurchaseEntity = purchaseRepository.findById(purchaseEntity.getPurchaseId());
                if (foundPurchaseEntity.isEmpty()) {
                    throw new RequestException("Couldn't find a purchase with provided ID", HttpStatus.NOT_FOUND);
                }

                if (purchaseEntity.getIsPaid() != 0) {
                    throw new RequestException("At least one of purchases is paid. Please verify that purchases that you want to merge are unpaid.", HttpStatus.BAD_REQUEST);
                }
                for (PurchaseBookEntity purchaseBookEntity : foundPurchaseEntity.get().getPurchasedBooks()) {
                    PurchaseBookEntity newPurchaseBookEntity = new PurchaseBookEntity();
                    newPurchaseBookEntity.setPurchaseId(newPurchaseEntity.getPurchaseId());
                    newPurchaseBookEntity.setAmount(purchaseBookEntity.getAmount());
                    newPurchaseBookEntity.setBookId(purchaseBookEntity.getBookId());
                    purchaseBookService.removePurchaseBook(purchaseBookEntity);
                    purchaseBookService.addPurchaseBook(newPurchaseBookEntity);
                }
                purchaseRepository.delete(foundPurchaseEntity.get());
            }
            purchaseRepository.save(newPurchaseEntity);
            return new ResponseEntity<>(newPurchaseEntity,new HeaderBuilder().setETag(""+newPurchaseEntity.hashCode()).setLocation(UriConstants.purchaseUri+"/"+newPurchaseEntity.getPurchaseId()).build(),HttpStatus.OK);
        } catch (RequestException re) {
            throw re;
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
