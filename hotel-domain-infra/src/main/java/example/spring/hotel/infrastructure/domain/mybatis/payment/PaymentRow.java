package example.spring.hotel.infrastructure.domain.mybatis.payment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentRow {
    private Long paymentId;
    private Long customerId;
    private Long checkoutId;
    private LocalDateTime paidDateTime;
    private long totalPrice;
}
