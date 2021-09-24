package example.spring.hotel.domain.model.payment;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    private PaymentType paymentType;
    private long price;
    private String accountNumber;
}
