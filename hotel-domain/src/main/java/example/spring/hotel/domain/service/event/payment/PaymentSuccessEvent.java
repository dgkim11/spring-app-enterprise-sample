package example.spring.hotel.domain.service.event.payment;

import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.service.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent implements DomainEvent {
    private Payment payment;

    public static String getEventKey() {
        return "event.domain.paymentSuccess";
    }
}
