package example.spring.hotel.domain.config;

import example.spring.hotel.domain.infrastructure.config.HotelAppInfraDatabaseConfig;
import example.spring.hotel.domain.infrastructure.config.HotelAppInfraPropertiesConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HotelAppInfraPropertiesConfig.class, HotelAppInfraDatabaseConfig.class })
public class HotelDomainConfig {
}
