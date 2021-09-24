package example.spring.hotel.domain.model.checkout;

import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService  {
    private PurchasedOrderRepository purchasedOrderRepository;

    public PurchaseHistoryServiceImpl(PurchasedOrderRepository purchasedOrderRepository)    {
        this.purchasedOrderRepository = purchasedOrderRepository;
    }

    @Override
    public boolean isPurchasedAlready(CheckoutItem checkoutItem) {
        Optional<PurchasedProduct> product =
                purchasedOrderRepository.findByProductIdAndBookingDateTime(checkoutItem.getProduct().getProductId(), checkoutItem.getBookingDateTime());
        return product.isPresent();
    }
}
