package example.spring.hotel.infrastructure.domain.config;

import example.spring.hotel.infrastructure.domain.helper.Helpers;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Helpers.class)
public class IntegrationTestConfig {
}
