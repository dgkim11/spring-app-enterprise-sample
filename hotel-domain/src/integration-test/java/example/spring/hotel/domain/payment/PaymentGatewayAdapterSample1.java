package example.spring.hotel.domain.payment;

import example.spring.hotel.domain.model.payment.PaymentGatewayAdapter;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.model.payment.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("PaymentGatewayAdapterSample1")
public class PaymentGatewayAdapterSample1 implements PaymentGatewayAdapter {
    @Override
    public String companyId() {
        return "PaymentGatewayAdapterSample1";
    }

    @Override
    public PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList) {
        return new PaymentResult(true, "");
    }
}
