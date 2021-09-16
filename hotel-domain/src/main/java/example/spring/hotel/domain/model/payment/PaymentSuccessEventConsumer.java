package example.spring.hotel.domain.model.payment;

import example.spring.hotel.domain.service.event.DomainEventConsumer;
import example.spring.hotel.domain.service.event.EventBroker;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentSuccessEventConsumer implements DomainEventConsumer<PaymentSuccessEvent> {
    private EventBroker eventBroker;
    private PaymentRepository paymentRepository;
    private String id;

    public PaymentSuccessEventConsumer(EventBroker eventBroker, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.eventBroker = eventBroker;

        subscribePaymentSuccessEvent(eventBroker);
    }

    private void subscribePaymentSuccessEvent(EventBroker eventBroker) {
        this.id = UUID.randomUUID().toString();
        try {
            eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), this);
        } catch (NotExistEventChannelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void consume(PaymentSuccessEvent event) {
        paymentRepository.insert(event.getPayment());
    }
}
