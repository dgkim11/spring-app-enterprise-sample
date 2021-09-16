package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutProductOptionRow {
    private Long checkoutItemId;
    private Long productOptionId;
    private long optionPrice;
}
