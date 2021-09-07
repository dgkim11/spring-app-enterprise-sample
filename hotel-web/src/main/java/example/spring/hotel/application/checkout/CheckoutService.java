package example.spring.hotel.application.checkout;

import example.spring.hotel.application.purchase.PurchaseService;
import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {
    private CheckoutRepository checkoutRepository;
    private PurchaseService purchaseService;
    private ProductRepository productRepository;

    public CheckoutService(CheckoutRepository checkoutRepository, PurchaseService purchaseService, ProductRepository productRepository)   {
        this.checkoutRepository = checkoutRepository;
        this.purchaseService = purchaseService;
        this.productRepository = productRepository;
    }

    public void checkout(Long customerId, CheckoutItem checkoutItem) throws CheckoutException {
        checkout(customerId, Arrays.asList(checkoutItem));
    }

    public Checkout checkout(Long customerId, List<CheckoutItem> checkoutItems) throws CheckoutException {
        long totalPrice = 0;
        for(CheckoutItem checkoutItem: checkoutItems)   {
            validateCheckoutItem(checkoutItem);
            totalPrice += getCheckoutItemPrice(checkoutItem);
        }
        return Checkout.builder()
                .customerId(customerId)
                .checkoutItem(checkoutItems)
                .totalPrice(totalPrice)
                .checkoutDateTime(LocalDateTime.now())
                .build();
    }

    private long getCheckoutItemPrice(CheckoutItem checkoutItem)    {
        long totalPrice = 0;
        totalPrice += checkoutItem.getProductPrice();
        totalPrice += checkoutItem.getCheckoutProductOptions().stream()
                .map(op -> op.getOptionPrice())
                .reduce(0L, Long::sum);
        return totalPrice;
    }

    private void validateCheckoutItem(CheckoutItem checkoutItem) throws CheckoutException {
        if(checkoutItem.getBookingDateTime() == null) throw new CheckoutException("예약 날짜가 없습니다.");
        if(isPurchasedAlready(checkoutItem)) throw new CheckoutException("이미 해당 예약날짜에 다른 예약이 존재합니다.");
        if(! isProductSellable(checkoutItem.getProductId())) throw new CheckoutException("판매되지 않는 상품이 존재합니다.");
    }

    private boolean isProductSellable(Long productId) {
        Optional<Product> productOptional = productRepository.findByIdWithoutOptions(productId);
        if(productOptional.isEmpty()) return false;
        return productOptional.get().isProductSellable();
    }

    private boolean isPurchasedAlready(CheckoutItem checkoutItem) {
        return purchaseService.isPurchased(checkoutItem.getProductId(), checkoutItem.getBookingDateTime());
    }
}
