package example.spring.hotel.domain.model.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutProductOption {
    private Long productOptionId;
    private long optionPrice;
}
