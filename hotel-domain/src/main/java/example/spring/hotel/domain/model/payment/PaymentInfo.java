package example.spring.hotel.domain.model.payment;

import lombok.Getter;

@Getter
public class PaymentInfo {
    private PaymentType paymentType;
    private long price;
    private String accountNumber;
}
