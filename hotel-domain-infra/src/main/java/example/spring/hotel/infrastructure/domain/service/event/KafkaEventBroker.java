package example.spring.hotel.infrastructure.domain.service.event;

import example.spring.hotel.domain.service.event.DomainEvent;
import example.spring.hotel.domain.service.event.DomainEventConsumer;
import example.spring.hotel.domain.service.event.AbstractEventBroker;
import example.spring.hotel.domain.service.event.EventChannel;

/**
 * Kafka로 구현한 event broker이다.
 */
public class KafkaEventBroker extends AbstractEventBroker {
    @Override
    public void subscribe(String eventName, DomainEventConsumer eventConsumer) {

    }

    @Override
    public void unSubscribe(String eventKey, DomainEventConsumer eventConsumer) {

    }

    @Override
    public void createEventChannel(String eventKey) {

    }

    @Override
    protected EventChannel createSpecificEventChannel() {
        return null;
    }

    @Override
    public void removeEventChannel(String eventKey) {

    }

    @Override
    public void sendEvent(String eventKey, DomainEvent event) {

    }
}
