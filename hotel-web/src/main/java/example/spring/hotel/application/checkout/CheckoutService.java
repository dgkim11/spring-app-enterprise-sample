package example.spring.hotel.application.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.checkout.PurchaseHistoryService;
import example.spring.hotel.domain.model.checkout.exception.CheckoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class CheckoutService {
    private PurchaseHistoryService purchaseHistoryService;
    private CheckoutRepository checkoutRepository;

    public CheckoutService(PurchaseHistoryService purchaseHistoryService, CheckoutRepository checkoutRepository)   {
        this.purchaseHistoryService = purchaseHistoryService;
        this.checkoutRepository = checkoutRepository;
    }

    public void checkout(Long customerId, CheckoutItem checkoutItem) throws CheckoutException {
        checkout(customerId, Arrays.asList(checkoutItem));
    }

    @Transactional
    public Checkout checkout(Long customerId, List<CheckoutItem> checkoutItems) throws CheckoutException {
        Checkout checkout = new Checkout(customerId);
        for(CheckoutItem checkoutItem: checkoutItems)   {
            checkout.addCheckoutItem(checkoutItem, purchaseHistoryService);
        }
        checkoutRepository.insert(checkout);
        return checkout;
    }
}
