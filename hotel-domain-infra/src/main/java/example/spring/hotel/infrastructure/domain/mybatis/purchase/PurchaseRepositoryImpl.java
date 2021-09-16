package example.spring.hotel.infrastructure.domain.mybatis.purchase;

import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.purchase.PurchaseRepository;
import example.spring.hotel.domain.model.purchase.PurchasedOrder;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {
    private PurchasedOrderMapper purchasedOrderMapper;
    private PurchasedProductMapper purchasedProductMapper;

    public PurchaseRepositoryImpl(PurchasedOrderMapper orderMapper, PurchasedProductMapper productMapper)   {
        this.purchasedOrderMapper = orderMapper;
        this.purchasedProductMapper = productMapper;
    }

    @Override
    public Optional<PurchasedOrder> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void insert(PurchasedOrder entity) {

    }

    @Override
    public Optional<PurchasedProduct> findByProductIdAndBookingDateTime(Long productId, LocalDateTime bookingDateTime) {
        return Optional.empty();
    }
}
