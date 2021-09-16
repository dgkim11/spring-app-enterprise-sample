package example.spring.hotel.domain.model.checkout;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.checkout.exception.CheckoutException;
import example.spring.hotel.domain.model.product.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Checkout implements DomainEntity  {
    private Long checkoutId;
    private Long customerId;
    private List<CheckoutItem> checkoutItems = new LinkedList<>();
    private long totalPrice = 0;
    LocalDateTime checkoutDateTime;

    public Checkout(Long customerId)    {
        this.customerId = customerId;
        this.checkoutDateTime = LocalDateTime.now();
    }

    public void addCheckoutItem(CheckoutItem checkoutItem, PurchaseHistoryService purchaseHistory) throws CheckoutException  {
        validateCheckoutItem(checkoutItem, purchaseHistory);
        Product product = checkoutItem.getProduct();
        totalPrice += product.getPrice() + checkoutItem.getCheckoutProductOptions().stream().mapToLong(o -> o.getOptionPrice()).sum();
        checkoutItems.add(checkoutItem);
    }

    private void validateCheckoutItem(CheckoutItem checkoutItem, PurchaseHistoryService purchaseHistoryService) throws CheckoutException {
        if(checkoutItem.getBookingDateTime() == null) throw new CheckoutException("예약 날짜가 없습니다.");
        if(purchaseHistoryService.isPurchasedAlready(checkoutItem)) throw new CheckoutException("이미 해당 예약날짜에 다른 예약이 존재합니다.");
        if(! checkoutItem.getProduct().isProductSellable()) throw new CheckoutException("판매되지 않는 상품이 존재합니다.");
    }
}
