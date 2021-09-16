package example.spring.hotel.application.customer;

import example.spring.hotel.domain.model.customer.Customer;
import example.spring.hotel.domain.model.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerControlService {
    private CustomerRepository customerRepository;

    public CustomerControlService(CustomerRepository customerRepository)    {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer)  {
        customerRepository.insert(customer);
        return customer;
    }

    public int removeCustomerById(Long customerId)  {
        return customerRepository.deleteById(customerId);
    }
}
