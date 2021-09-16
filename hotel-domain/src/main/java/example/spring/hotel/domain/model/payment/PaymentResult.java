package example.spring.hotel.domain.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private boolean success;
    private String message;
}
