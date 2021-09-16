package example.spring.hotel.web.config;

import example.spring.hotel.application.ApplicationServices;
import example.spring.hotel.domain.HotelDomains;
import example.spring.hotel.domain.config.HotelDomainConfig;
import example.spring.hotel.infrastructure.domain.config.HotelAppDomainInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {ApplicationServices.class, HotelDomains.class})
@Import({HotelDomainConfig.class, HotelAppDomainInfraConfig.class})
public class HotelApplicationWebConfig {
}
