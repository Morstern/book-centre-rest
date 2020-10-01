package pl.kamil.zielinski.bookcentrerest.purchase;

import lombok.Getter;
import pl.kamil.zielinski.bookcentrerest.purchase_book.PurchaseBookDto;

import java.sql.Date;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class PurchaseDto {
    private int purchaseId;
    private Integer isPaid;
    private Date orderDate;
    private Collection<PurchaseBookDto> purchasedBooks;

    public PurchaseDto(PurchaseEntity purchaseEntity){
        this.purchaseId = purchaseEntity.getPurchaseId();
        this.isPaid = purchaseEntity.getIsPaid();
        this.orderDate = purchaseEntity.getOrderDate();
        this.purchasedBooks = purchaseEntity.getPurchasedBooks().stream().map(purchaseBookEntity -> new PurchaseBookDto(purchaseBookEntity)).collect(Collectors.toList());
    }
}
