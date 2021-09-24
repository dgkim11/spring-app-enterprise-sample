package example.spring.hotel.domain.service.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Guava의 event bus를 이용해서 구현한다.
 */
@Component
public class DefaultEventBroker extends AbstractEventBroker {
    @Override
    protected EventChannel createSpecificEventChannel() {
        return new GuavaEventChannel();
    }

    private static class GuavaEventChannel implements EventChannel  {
        private Map<String, GuavaEventListener> listenerMap = new HashMap<>();
        private EventBus eventBus;

        public GuavaEventChannel()  {
            this.eventBus = new EventBus();
        }

        @Override
        public void addEventConsumer(DomainEventConsumer eventConsumer) {
            GuavaEventListener listener = new GuavaEventListener(eventConsumer);
            eventBus.register(listener);
            listenerMap.put(eventConsumer.getId(), listener);
        }

        @Override
        public void removeEventConsumer(DomainEventConsumer eventConsumer) {
            GuavaEventListener listener = listenerMap.get(eventConsumer.getId());
            eventBus.unregister(listener);
            listenerMap.remove(eventConsumer.getId());
        }

        @Override
        public void removeAll() {
            listenerMap.values().stream().peek(eventBus::unregister).count();
            listenerMap.clear();
        }

        @Override
        public void sendEvent(DomainEvent event) {
            eventBus.post(event);
        }
    }

    private static class GuavaEventListener {
        private DomainEventConsumer eventConsumer;

        public GuavaEventListener(DomainEventConsumer eventConsumer)  {
            this.eventConsumer = eventConsumer;
        }

        @Subscribe
        public void handle(DomainEvent domainEvent) {
            eventConsumer.consume(domainEvent);
        }
    }


}
