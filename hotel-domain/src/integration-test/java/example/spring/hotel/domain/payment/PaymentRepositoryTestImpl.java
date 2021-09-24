package example.spring.hotel.domain.payment;

import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentRepositoryTestImpl implements PaymentRepository {
    private Payment payment;
    private boolean inserted;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    public boolean isInserted() { return this.inserted; }

    @Override
    public Optional<Payment> findById(Long aLong) {
        return Optional.of(payment);
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void insert(Payment entity) {
        this.inserted = true;
    }

    @Override
    public void deleteByCustomerId(Long customerId) {

    }
}
