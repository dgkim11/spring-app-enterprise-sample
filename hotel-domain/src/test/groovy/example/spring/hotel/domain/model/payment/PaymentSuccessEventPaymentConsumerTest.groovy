package example.spring.hotel.domain.model.payment

import example.spring.hotel.domain.service.event.EventBroker
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent
import spock.lang.Specification

class PaymentSuccessEventPaymentConsumerTest extends Specification  {
    private EventBroker eventBroker
    private PaymentRepository paymentRepository

    def setup() {
        eventBroker = Stub(EventBroker)
        paymentRepository = Mock(PaymentRepository)
    }

    def "결제가 성공 이벤트를 받으면 결제 내역에 대한 정보를 저장해야 한다."() {
        PaymentSuccessEventPaymentConsumer consumer = new PaymentSuccessEventPaymentConsumer(eventBroker, paymentRepository)

        given: "결제 성공 이벤트 발생"
        Payment payment = new Payment(paymentId:10L)
        PaymentSuccessEvent event = new PaymentSuccessEvent(payment)

        when: "이벤트를 소비한다."
        consumer.consume(event)

        then: "payment 데이터를 저장한다."
        1* paymentRepository.insert(payment)
    }
}
