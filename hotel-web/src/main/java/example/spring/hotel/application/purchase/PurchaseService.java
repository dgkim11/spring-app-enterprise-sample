package example.spring.hotel.application.purchase;

import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.model.purchase.PurchaseRepository;
import example.spring.hotel.domain.model.purchase.PurchasedOrder;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * checkout에서 결재까지 완료한 후에 최종 구매 처리를 진행하는 서비스이다.
 */
@Service
public class PurchaseService {
    private PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository)   {
        this.purchaseRepository = purchaseRepository;
    }

    public void purchased(List<CheckoutItem> checkoutItems, PaymentInfo paymentInfo)    {

    }

    public PurchasedOrder findById(Long purchasedItemId)  {
        return null;
    }

    /**
     *
     * @param productId
     * @param bookingDateTime
     * @return
     */
    public boolean isPurchased(Long productId, LocalDateTime bookingDateTime) {
        Optional<PurchasedProduct> product = purchaseRepository.findByProductIdAndBookingDateTime(productId, bookingDateTime);
        return product.isEmpty();
    }
}
