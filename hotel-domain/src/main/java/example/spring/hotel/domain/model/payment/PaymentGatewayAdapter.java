package example.spring.hotel.domain.model.payment;

import java.util.List;

public interface PaymentGatewayAdapter {
    /**
     * 특정 PG사 고유 Id. 해당 ID로 PG를 구분한다.
     * @return
     */
    String companyId();
    PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList);
}
