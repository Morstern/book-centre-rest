package pl.kamil.zielinski.bookcentrerest.purchase;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends PagingAndSortingRepository<PurchaseEntity, Integer> {
}
