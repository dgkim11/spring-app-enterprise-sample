package example.spring.hotel.domain.model.purchase;

import example.spring.hotel.domain.model.BaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PurchaseRepository extends BaseRepository<PurchasedOrder, Long> {
    Optional<PurchasedProduct> findByProductIdAndBookingDateTime(Long productId, LocalDateTime bookingDateTime);
}
