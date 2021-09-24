package example.spring.hotel.infrastructure.domain.mybatis.payment;

import example.spring.hotel.domain.model.payment.PaymentType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentInfoRow {
    private Long paymentId;
    private PaymentType paymentType;
    private long price;
    private String accountNumber;
}
