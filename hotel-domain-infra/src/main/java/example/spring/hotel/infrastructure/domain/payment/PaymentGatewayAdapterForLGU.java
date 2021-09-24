package example.spring.hotel.infrastructure.domain.payment;

import example.spring.hotel.domain.model.payment.PaymentGatewayAdapter;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.model.payment.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 외부 시스템과의 통신은 infra layer에서 구현한다. domain layer는 interface를 제공하고 실제 구현은 infra layer에서 구현한다.
 * 이렇게 함으로써 domain layer는 외부 시스템과의 의존성에서 벗어날 수 있고, 이러한 외부 시스템 없이도 unit test를 진행할 수 있다.
 */
@Component("paymentAda")
public class PaymentGatewayAdapterForLGU implements PaymentGatewayAdapter {
    @Override
    public String companyId() {
        return "LGU";
    }

    @Override
    public PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList) {
        return new PaymentResult(true, "");
    }
}
