package example.spring.hotel.domain.config;

import example.spring.hotel.domain.service.event.EventBroker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class HotelDomainConfig {
    @Value("${domain.event.broker:example.spring.hotel.domain.service.event.DefaultEventBroker}")
    private String eventBrokerClass;

    @Bean
    public EventBroker eventBroker()    {
        try {
            Object eventBroker = Class.forName(eventBrokerClass).getDeclaredConstructor().newInstance();
            return (EventBroker) eventBroker;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
