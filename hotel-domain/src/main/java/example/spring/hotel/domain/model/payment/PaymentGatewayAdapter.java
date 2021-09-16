package example.spring.hotel.domain.model.payment;

import java.util.List;

public interface PaymentGatewayAdapter {
    PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList);
}
