package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import example.spring.hotel.domain.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItemRow {
    private Long checkoutItemId;
    private Long productId;
    private long productPrice;
    private LocalDateTime bookingDateTime;
}
