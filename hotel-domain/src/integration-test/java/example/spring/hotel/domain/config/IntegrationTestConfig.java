package example.spring.hotel.domain.config;

import example.spring.hotel.domain.BeansForTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BeansForTest.class)
public class IntegrationTestConfig {
}
