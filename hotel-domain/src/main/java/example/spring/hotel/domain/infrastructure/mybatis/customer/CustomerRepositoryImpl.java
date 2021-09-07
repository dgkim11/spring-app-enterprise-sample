package example.spring.hotel.domain.infrastructure.mybatis.customer;

import example.spring.hotel.domain.model.customer.Customer;
import example.spring.hotel.domain.model.customer.CustomerRepository;
import example.spring.hotel.domain.model.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository  {
    private CustomerMapper mapper;

    public CustomerRepositoryImpl(CustomerMapper mapper)    {
        this.mapper = mapper;
    }

    @Override
    public Optional<Customer> findById(Long customerId) {
        return Optional.ofNullable(mapper.findById(customerId));
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void save(Customer entity) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
