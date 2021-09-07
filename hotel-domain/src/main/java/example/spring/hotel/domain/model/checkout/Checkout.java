package example.spring.hotel.domain.model.checkout;

import example.spring.hotel.domain.model.DomainEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checkout implements DomainEntity  {
    private Long checkoutId;
    private Long customerId;
    private List<CheckoutItem> checkoutItem;
    private long totalPrice;
    LocalDateTime checkoutDateTime;
}
