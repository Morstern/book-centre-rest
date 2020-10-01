package pl.kamil.zielinski.bookcentrerest.purchase_book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBookEntityPK implements Serializable {
    private int purchaseId;
    private int bookId;

    @Column(name = "purchase_id", nullable = false)
    @Id
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Column(name = "book_id", nullable = false)
    @Id
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseBookEntityPK that = (PurchaseBookEntityPK) o;
        return purchaseId == that.purchaseId &&
                bookId == that.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, bookId);
    }
}
