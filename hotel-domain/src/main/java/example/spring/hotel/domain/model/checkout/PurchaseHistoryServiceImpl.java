package example.spring.hotel.domain.model.checkout;

import example.spring.hotel.domain.model.purchase.PurchaseRepository;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService  {
    private PurchaseRepository purchaseRepository;

    public PurchaseHistoryServiceImpl(PurchaseRepository purchaseRepository)    {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public boolean isPurchasedAlready(CheckoutItem checkoutItem) {
        Optional<PurchasedProduct> product =
                purchaseRepository.findByProductIdAndBookingDateTime(checkoutItem.getProduct().getProductId(), checkoutItem.getBookingDateTime());
        return product.isEmpty();
    }
}
