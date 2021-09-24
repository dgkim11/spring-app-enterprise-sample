package example.spring.hotel.domain.purchase;

import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository;
import example.spring.hotel.domain.model.purchase.PurchasedOrder;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PurchasedOrderRepositoryTestImpl implements PurchasedOrderRepository {
    private PurchasedOrder purchasedOrder;
    private boolean inserted;

    public boolean isInserted() { return this.inserted; }

    @Override
    public Optional<PurchasedOrder> findById(Long aLong) {
        return Optional.of(purchasedOrder);
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void insert(PurchasedOrder entity) {
        this.inserted = true;
    }

    @Override
    public Optional<PurchasedProduct> findByProductIdAndBookingDateTime(Long productId, LocalDateTime bookingDateTime) {
        return Optional.empty();
    }
}
