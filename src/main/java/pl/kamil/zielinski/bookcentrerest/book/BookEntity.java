package pl.kamil.zielinski.bookcentrerest.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "bookcentre")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {
    private int bookId;
    private String title;
    private Integer amount;
    private String author;
    private String description;
    private BigDecimal price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }


    @Basic
    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Basic
    @Column(name = "amount")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    @Basic
    @Column(name = "author", length = 100)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    @Basic
    @Column(name = "description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Basic
    @Column(name = "price", precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return bookId == that.bookId &&
                Objects.equals(title, that.title) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(author, that.author) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, amount, author, description, price);
    }

}
