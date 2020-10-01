package pl.kamil.zielinski.bookcentrerest.purchase_book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseBookRepository extends JpaRepository<PurchaseBookEntity, PurchaseBookEntityPK> {
}
