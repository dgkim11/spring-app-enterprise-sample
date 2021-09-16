package example.spring.hotel.infrastructure.domain.mybatis.customer

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.customer.Customer
import example.spring.hotel.domain.model.customer.CustomerRepository
import example.spring.hotel.infrastructure.domain.config.HotelAppDomainInfraConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelAppDomainInfraConfig.class])
class CustomerIntegrationTest extends Specification  {
    @Autowired private CustomerRepository customerRepository

    def "생성한 고객을 조회한다."()   {
        String userId = "CustomerIntegrationTest.customer"
        given: "고객을 생성한다."
        Customer customer = createCustomer(userId)
        customerRepository.insert(customer)

        when: "고객을 조회한다."
        Optional<Customer> customerOptional = customerRepository.findById(customer.getCustomerId())

        then: "고객이 존재한다."
        customerOptional.isPresent()
        customerOptional.get().getUserId() == userId

        cleanup:
        customerRepository.deleteById(customer.getCustomerId())
    }

    def "생성한 고객을 삭제한다."()   {
        String userId = "CustomerIntegrationTest.customer"

        given:"고객을 생성한 후 삭제한다."
        Customer customer = createCustomer(userId)
        customerRepository.insert(customer)
        customerRepository.deleteById(customer.getCustomerId())

        when:"고객을 조회한다."
        Optional<Customer> customerOptional = customerRepository.findById(customer.getCustomerId())

        then:"고객이 존재하지 않는다."
        customerOptional.isEmpty()

        cleanup:
        customerRepository.deleteById(customer.getCustomerId())
    }

    Customer createCustomer(String userId)  {
        return Customer.builder()
                .userId(userId)
                .password("password")
                .name("name")
                .emailAddr("email@mail.com")
                .build()
    }
}
