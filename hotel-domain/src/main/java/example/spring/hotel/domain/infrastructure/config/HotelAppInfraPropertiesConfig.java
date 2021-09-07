package example.spring.hotel.domain.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * active profile에 맞추어 PropertySource를 지정한다.
 */
@Configuration
@Slf4j
public class HotelAppInfraPropertiesConfig {
    @Profile("develop")
    @PropertySource({ "classpath:hotel-infra-common.xml", "classpath:hotel-infra-develop.xml"})
    public static class DevelopProperties  {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfig() {
            log.info("Current profile : develop");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Profile("production")
    @PropertySource({ "classpath:hotel-infra-common.xml", "classpath:hotel-infra-production.xml"})
    public static class ProductionProperties   {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfig() {
            log.info("Current profile : production");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Profile("local")
    @PropertySource({ "classpath:hotel-infra-common.xml", "classpath:hotel-infra-local.xml"})
    public static class LocalProperties  {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfig() {
            log.info("Current profile : local");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}
