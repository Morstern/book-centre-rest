package pl.kamil.zielinski.bookcentrerest.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import pl.kamil.zielinski.bookcentrerest.client.ClientEntity;
import pl.kamil.zielinski.bookcentrerest.purchase_book.PurchaseBookEntity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;


@Entity
@JsonIgnoreProperties({"client"})
@Table(name = "purchase", schema = "bookcentre")
public class PurchaseEntity {
    private int purchaseId;
    private Integer clientId;
    private Integer isPaid;
    private Date orderDate;
    private ClientEntity client;
    private Collection<PurchaseBookEntity> purchasedBooks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id", nullable = false)
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Basic
    @Column(name = "client_id")
    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Column(name = "is_paid", columnDefinition = "integer default 0")
    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    @Basic
    @CreatedDate
    @Column(name = "order_date")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseEntity that = (PurchaseEntity) o;
        return purchaseId == that.purchaseId &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(isPaid, that.isPaid) &&
                Objects.equals(orderDate, that.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, clientId, isPaid, orderDate);
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", table = "purchase", insertable = false, updatable = false)
    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "purchase")
    public Collection<PurchaseBookEntity> getPurchasedBooks() {
        return purchasedBooks;
    }

    public void setPurchasedBooks(Collection<PurchaseBookEntity> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }

    @Override
    public String toString() {
        return "PurchaseEntity{" +
                "purchaseId=" + purchaseId +
                ", clientId=" + clientId +
                ", isPaid=" + isPaid +
                ", orderDate=" + orderDate +
                '}';
    }
    

}
