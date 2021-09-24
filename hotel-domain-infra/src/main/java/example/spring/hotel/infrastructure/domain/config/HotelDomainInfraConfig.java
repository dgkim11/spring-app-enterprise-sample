package example.spring.hotel.infrastructure.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HotelAppInfraPropertiesConfig.class, HotelAppInfraDatabaseConfig.class })
public class HotelDomainInfraConfig {
    @Bean
    public ObjectMapper objectMapper()  {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
