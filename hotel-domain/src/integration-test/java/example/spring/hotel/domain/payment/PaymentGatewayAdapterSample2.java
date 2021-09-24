package example.spring.hotel.domain.payment;

import example.spring.hotel.domain.model.payment.PaymentGatewayAdapter;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.model.payment.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("PaymentGatewayAdapterSample2")
public class PaymentGatewayAdapterSample2 implements PaymentGatewayAdapter {
    @Override
    public String companyId() {
        return "PaymentGatewayAdapterSample2";
    }

    @Override
    public PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList) {
        return new PaymentResult(true, "");
    }
}
