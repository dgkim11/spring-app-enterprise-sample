package example.spring.hotel.domain.service.event;

import example.spring.hotel.domain.service.event.exception.AlreadyExistEventChannelException;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEventBroker implements EventBroker {
    private Map<String, EventChannel> eventChannelMap = new HashMap<>();

    @Override
    public void createEventChannel(String eventKey) throws AlreadyExistEventChannelException {
        if(eventChannelMap.containsKey(eventKey)) throw new AlreadyExistEventChannelException("eventKey:" + eventKey);
        eventChannelMap.put(eventKey, createSpecificEventChannel());
    }

    /**
     * EventBroker 구현체마다 자신만의 EventChannel을 구현체를 갖는다.
     * @return
     */
    protected abstract EventChannel createSpecificEventChannel();

    @Override
    public void removeEventChannel(String eventKey)    {
        if(eventChannelMap.get(eventKey) != null)   {
            EventChannel eventChannel = eventChannelMap.get(eventKey);
            eventChannel.removeAll();
            eventChannelMap.remove(eventKey);
        }
    }

    @Override
    public void subscribe(String eventKey, DomainEventConsumer eventConsumer) throws NotExistEventChannelException {
        if(! eventChannelMap.containsKey(eventKey)) throw new NotExistEventChannelException("eventKey:" + eventKey);
        EventChannel eventChannel = eventChannelMap.get(eventKey);
        eventChannel.addEventConsumer(eventConsumer);
    }

    @Override
    public void unSubscribe(String eventKey, DomainEventConsumer eventConsumer) throws NotExistEventChannelException {
        if(! eventChannelMap.containsKey(eventKey)) throw new NotExistEventChannelException("eventKey:" + eventKey);
        EventChannel eventChannel = eventChannelMap.get(eventKey);
        eventChannel.removeEventConsumer(eventConsumer);
    }

    @Override
    public void sendEvent(String eventKey, DomainEvent event) throws NotExistEventChannelException {
        if(! eventChannelMap.containsKey(eventKey)) throw new NotExistEventChannelException("eventKey:" + eventKey);
        EventChannel eventChannel = eventChannelMap.get(eventKey);
        eventChannel.sendEvent(event);
    }
}
