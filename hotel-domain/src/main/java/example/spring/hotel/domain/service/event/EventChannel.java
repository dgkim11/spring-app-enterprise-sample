package example.spring.hotel.domain.service.event;

public interface EventChannel {
    void addEventConsumer(DomainEventConsumer eventConsumer);
    void removeEventConsumer(DomainEventConsumer eventConsumer);
    void removeAll();
    void sendEvent(DomainEvent event);
}
