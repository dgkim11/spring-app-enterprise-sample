package example.spring.hotel.domain.service.event;

import example.spring.hotel.domain.service.event.exception.AlreadyExistEventChannelException;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;

public interface EventBroker {
    /**
     * 신규 event 채널을 오픈(publish) 한다.
     * @param eventKey 신규 event 채널의 eventKey
     */
    void createEventChannel(String eventKey) throws AlreadyExistEventChannelException;

    void removeEventChannel(String eventKey);

    /**
     * 해당 event key 채널에 대해서 구독
     * @param eventKey 채널 key
     * @param eventConsumer
     */
    void subscribe(String eventKey, DomainEventConsumer eventConsumer) throws NotExistEventChannelException;

    /**
     * 해당 event key 채널에 대해서 구독
     * @param eventKey 채널 key
     * @param eventConsumer
     */
    void unSubscribe(String eventKey, DomainEventConsumer eventConsumer) throws NotExistEventChannelException;

    /**
     * event 채널로 event 발송
     * @param eventKey 채널 key
     * @param event
     */
    void sendEvent(String eventKey, DomainEvent event) throws NotExistEventChannelException;
}
