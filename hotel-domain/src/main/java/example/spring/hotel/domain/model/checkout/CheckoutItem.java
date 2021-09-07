package example.spring.hotel.domain.model.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItem {
    private Long productId;
    private long productPrice;
    private List<CheckoutProductOption> checkoutProductOptions = new LinkedList<>();
    private LocalDateTime bookingDateTime;
}
