package example.spring.hotel.domain.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    private PaymentType paymentType;
    private long price;
    private String accountNumber;
}
