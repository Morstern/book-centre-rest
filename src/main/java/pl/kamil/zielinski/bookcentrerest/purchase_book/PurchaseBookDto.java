package pl.kamil.zielinski.bookcentrerest.purchase_book;

import lombok.Getter;

@Getter
public class PurchaseBookDto {
    private int bookId;
    private int amount;

    public PurchaseBookDto(PurchaseBookEntity purchaseBookEntity){
        this.bookId = purchaseBookEntity.getBookId();
        this.amount = purchaseBookEntity.getAmount();
    }
}
