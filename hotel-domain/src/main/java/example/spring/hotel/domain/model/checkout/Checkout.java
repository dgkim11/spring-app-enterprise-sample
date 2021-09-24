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
    @Builder.Default
    private List<CheckoutItem> checkoutItems = new LinkedList<>();
    @Builder.Default
    private long totalPrice = 0;
    LocalDateTime checkoutDateTime;

    public Checkout(Long customerId)    {
        this.customerId = customerId;
        this.checkoutDateTime = LocalDateTime.now();
        this.checkoutItems = new LinkedList<>();
    }

    /**
     * 두번째 파라미터에 대해서 설명하면, Checkout 시에 이미 해당 상품이 해당 날짜에 예약(구매)가 된 것인지에 대한 판단이 필요하다.
     * 그러기 위해서는 Checkout 도메인이 다른쪽 도메인 객체를 참조하여야 한다. 여기서 DDD의 철학이 중요하다. 도메인 모델은 다른 도메인 모델이나
     * 다른 도메인의 리파지토리를 직접 참조하지 않는다. 따라서, PurchaseHistoryService interface를 주입받아서 Checkout 도메인 밖에서
     * 해당 작업이 이루어지도록 한다.
     * @param checkoutItem
     * @param purchaseHistory
     * @throws CheckoutException
     */
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
