package example.spring.hotel.infrastructure.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HotelAppInfraPropertiesConfig.class, HotelAppInfraDatabaseConfig.class })
public class HotelAppDomainInfraConfig {
}
