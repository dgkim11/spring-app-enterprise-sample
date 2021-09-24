package example.spring.hotel.domain.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CheckoutRepositoryTestImpl implements CheckoutRepository {
    private Checkout checkout;
    private boolean called;

    public void setCheckout(Checkout checkout)  {
        this.checkout = checkout;
    }
    public boolean isCalled()   { return this.called; }

    @Override
    public Optional<Checkout> findById(Long aLong) {
        this.called = true;
        return Optional.of(checkout);
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void insert(Checkout entity) {

    }
}
