package example.spring.hotel.domain.model.checkout;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItem implements DomainEntity  {
    private Long checkoutId;
    private Long checkoutItemId;
    private Product product;
    private long productPrice;  // snapshot. checkout 시점의 가격. 이후 가격이 바뀌더라도 고객은 이 가격으로 주문할 수 있다.
    @Builder.Default
    private List<CheckoutProductOption> checkoutProductOptions = new LinkedList<>();
    private LocalDateTime bookingDateTime;
}
