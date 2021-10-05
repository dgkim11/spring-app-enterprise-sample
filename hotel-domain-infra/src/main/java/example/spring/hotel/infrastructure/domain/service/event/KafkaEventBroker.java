package example.spring.hotel.infrastructure.domain.service.event;

import example.spring.hotel.domain.service.event.DomainEvent;
import example.spring.hotel.domain.service.event.DomainEventConsumer;
import example.spring.hotel.domain.service.event.AbstractEventBroker;
import example.spring.hotel.domain.service.event.EventChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Kafka로 구현한 event broker이다. 미완성.
 */
public class KafkaEventBroker extends AbstractEventBroker {
    @Override
    public void subscribe(String eventName, DomainEventConsumer eventConsumer) {
//        Properties config = new Properties();
//        config.put("client.id", InetAddress.getLocalHost().getHostName());
//        config.put("group.id", "foo");
//        config.put("bootstrap.servers", "host1:9092,host2:9092");
//        new KafkaConsumer(config);
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
//        Properties config = new Properties();
//        try {
//            config.put("client.id", InetAddress.getLocalHost().getHostName());
//            config.put("bootstrap.servers", "host1:9092,host2:9092");
//            config.put("acks", "all");
//            new KafkaProducer(config);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

    }
}
