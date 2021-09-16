package example.spring.hotel.domain.service.event

import example.spring.hotel.domain.service.event.exception.AlreadyExistEventChannelException
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent
import spock.lang.Specification

class DefaultEventBrokerTest extends Specification  {
    def "이미 존재하는 eventKey로 채널을 만들 수 없다."() {
        EventBroker eventBroker = new DefaultEventBroker()
        given: "신규 event 채널을 연다."
        eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey())
        when: "동일 event key르 채널을 연다."
        eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey())
        then: "채널을 열 수 없다."
        thrown(AlreadyExistEventChannelException)
    }

    def "존재하지 않는 event 채널을 구독할 수 없다."() {
        given: "열린 채널이 없다."
        when: "존재하지 않는 채널에 구독한다."
        then: "채널에 구독할 수 없다."
    }
    def "존재하지 않는 event 채널에 event 발송할 수 없다."()   {
        given: "열린 채널이 없다."
        when: "존재하지 않는 채널로 발송한다."
        then: "발송할 수 없다."
    }

    def "event를 발송하고 성공적으로 event를 consume 한다."()    {
        EventBroker eventBroker = new DefaultEventBroker()
        PaymentSuccessEventConsumerImpl consumer = new PaymentSuccessEventConsumerImpl("consumer")
        eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey())

        given: "event를 구독한다."
        eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), consumer)

        when: "event를 발송한다."
        eventBroker.sendEvent(PaymentSuccessEvent.getEventKey(),  new PaymentSuccessEvent(paymentId: 1234L))

        then: "event를 받는다."
        consumer.event.getPaymentId() == 1234L
    }

    def "모든 consumer들에게 event를 동일하게 발송한다."()    {
        EventBroker eventBroker = new DefaultEventBroker()
        PaymentSuccessEventConsumerImpl consumer1= new PaymentSuccessEventConsumerImpl("consumer1")
        PaymentSuccessEventConsumerImpl consumer2 = new PaymentSuccessEventConsumerImpl("consumer2")
        PaymentSuccessEventConsumerImpl consumer3 = new PaymentSuccessEventConsumerImpl("consumer3")
        eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey())

        given: "두개 이상의 consumer가 동일 event에 구독한다."
        eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), consumer1)
        eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), consumer2)
        eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), consumer3)

        when: "event를 발송한다."
        eventBroker.sendEvent(PaymentSuccessEvent.getEventKey(),  new PaymentSuccessEvent(paymentId: 1234L))

        then: "모든 consumer들이 동일 event를 받는다."
        consumer1.event.getPaymentId() == 1234L
        consumer2.event.getPaymentId() == 1234L
        consumer3.event.getPaymentId() == 1234L
    }

    def "event를 unsubscribe하는 경우 event를 받지 않는다."()  {
        EventBroker eventBroker = new DefaultEventBroker()
        PaymentSuccessEventConsumerImpl consumer = new PaymentSuccessEventConsumerImpl("consumer")
        eventBroker.createEventChannel(PaymentSuccessEvent.getEventKey())

        given: "구독된 event consumer가 있다."
        eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), consumer)
        and: "구독을 해지한다."
        eventBroker.unSubscribe(PaymentSuccessEvent.getEventKey(), consumer)

        when: "event를 발송한다."
        eventBroker.sendEvent(PaymentSuccessEvent.getEventKey(), new PaymentSuccessEvent(paymentId: 1234L))

        then: "해당 consumer는 event를 받지 않는다."
        consumer.event == null
    }

    def "event channel이 사라지면 더 이상 event를 발송할 수 없다."()  {
        given: "event channel에 consumer가 있다."
        and: "해당 event channel을 제거한다."
        when: "event를 발송한다."
        then: "event를 발송할 수 없다."
    }

    private static class PaymentSuccessEventConsumerImpl implements DomainEventConsumer<PaymentSuccessEvent> {
        private String id;
        private PaymentSuccessEvent event

        PaymentSuccessEventConsumerImpl(String id) {
            this.id = id
        }

        @Override
        String getId() {
            return this.id
        }

        @Override
        void consume(PaymentSuccessEvent event) {
            this.event = event
        }
    }
}
