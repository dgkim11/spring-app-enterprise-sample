package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRow {
    private Long checkoutId;
    private Long customerId;
    private long totalPrice;
    LocalDateTime checkoutDateTime;
}
