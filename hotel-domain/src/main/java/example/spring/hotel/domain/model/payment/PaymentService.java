package example.spring.hotel.domain.model.payment;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.payment.exception.PaymentException;
import example.spring.hotel.domain.service.event.EventBroker;
import example.spring.hotel.domain.service.event.exception.AlreadyExistEventChannelException;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private PaymentGatewayAdapter paymentGatewayAdapter;
    private EventBroker eventBroker;

    public PaymentService(PaymentGatewayAdapter adapter, EventBroker eventBroker)    {
        this.paymentGatewayAdapter = adapter;
        try {
            this.eventBroker = eventBroker;
            eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey());
        } catch (AlreadyExistEventChannelException e) {
            throw new RuntimeException("event broker 생성 에러.", e);
        }
    }

    public Payment pay(Checkout checkout, List<PaymentInfo> paymentInfoList) throws PaymentException {
        validatePaymentInfo(checkout, paymentInfoList);
        PaymentResult result = paymentGatewayAdapter.execute(checkout.getTotalPrice(), paymentInfoList);
        if(result.isSuccess())  {
            Payment payment = Payment.builder()
                    .paymentInfos(paymentInfoList)
                    .checkoutId(checkout.getCheckoutId())
                    .paidDateTime(LocalDateTime.now())
                    .totalPrice(checkout.getTotalPrice())
                    .build();
            try {
                // payment gateway를 통해 결제가 완료된 후에 업데이트 되어야 하는 데이타가 하나의 트랜잭션 처럼 동작하려면
                // event 기반의 eventual consistency 개념을 적용해서 단일 트랜잭션 처럼 동작하도록 할 수 있다.
                // 좀 더 복잡한 내용은 Saga 패턴을 참조하기 바람.
                eventBroker.sendEvent(PaymentSuccessEvent.getEventKey(), new PaymentSuccessEvent(payment));
            } catch (NotExistEventChannelException e) {
                e.printStackTrace();
            }
        }

        throw new PaymentException("결제 실패. message:" + result.getMessage());

    }

    private void validatePaymentInfo(Checkout checkout, List<PaymentInfo> paymentInfoList) throws PaymentException {
        long totalPrice = 0;
        Map<PaymentType, String> paymentTypeMap = new HashMap<>();
        for(PaymentInfo paymentInfo : paymentInfoList)  {
            totalPrice += paymentInfo.getPrice();
            if(paymentTypeMap.get(paymentInfo.getPaymentType()) != null)
                throw new PaymentException("동일한 결제타입이 여러개 존재합니다.");
            paymentTypeMap.put(paymentInfo.getPaymentType(), "");
        }
        if(totalPrice != checkout.getTotalPrice())
            throw new PaymentException("총 금액이 맞지 않습니다.");
    }
}
