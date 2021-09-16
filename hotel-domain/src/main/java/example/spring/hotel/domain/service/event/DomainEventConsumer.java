package example.spring.hotel.domain.service.event;

public interface DomainEventConsumer<T extends DomainEvent> {
    /**
     * 모든 consumer는 id가 unique 해야 한다.
     * @return
     */
    String getId();
    void consume(T domainEvent);
}
