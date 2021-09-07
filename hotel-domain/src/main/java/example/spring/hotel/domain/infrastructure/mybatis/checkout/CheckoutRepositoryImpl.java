package example.spring.hotel.domain.infrastructure.mybatis.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CheckoutRepositoryImpl implements CheckoutRepository {
    @Override
    public Optional<Checkout> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void save(Checkout entity) {

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
