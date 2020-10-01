package pl.kamil.zielinski.bookcentrerest.purchase_book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "purchase_book", schema = "bookcentre")
@IdClass(PurchaseBookEntityPK.class)
@JsonIgnoreProperties({"purchase"})
public class PurchaseBookEntity {
    private int purchaseId;
    private int bookId;
    private Integer amount;
    private PurchaseEntity purchase;

    @Id
    @Column(name = "purchase_id", nullable = false)
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Id
    @Column(name = "book_id", nullable = false)
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Basic
    @Column(name = "amount", nullable = true)
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseBookEntity that = (PurchaseBookEntity) o;
        return purchaseId == that.purchaseId &&
                bookId == that.bookId &&
                Objects.equals(amount, that.amount);}

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, bookId, amount);
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id", nullable = false, table = "purchase_book", insertable = false, updatable = false)
    public PurchaseEntity getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseEntity purchase) {
        this.purchase = purchase;
    }


    @Override
    public String toString() {
        return "PurchaseBookEntity{" +
                "purchaseId=" + purchaseId +
                ", bookId=" + bookId +
                ", amount=" + amount +
                '}';
    }
}
