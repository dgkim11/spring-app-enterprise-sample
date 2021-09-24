package example.spring.hotel.web.controller;

import example.spring.hotel.domain.model.payment.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentRequest {
    private String gatewayCompanyId;
    private Long checkoutId;
    private List<PaymentInfo> paymentInfoList;
}
