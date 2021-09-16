package example.spring.hotel.domain.model.checkout;

public interface PurchaseHistoryService {
    boolean isPurchasedAlready(CheckoutItem checkoutItem);
}
