package example.spring.hotel.domain.model.payment;

import java.time.LocalDateTime;
import java.util.List;

public class Payment {
    private Long paymentId;
    private Long checkoutId;
    private List<PaymentInfo> paymentInfos;
    private LocalDateTime paidDateTime;
    private long totalPrice;
}
