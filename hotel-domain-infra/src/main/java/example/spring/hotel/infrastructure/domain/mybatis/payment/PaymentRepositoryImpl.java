package example.spring.hotel.infrastructure.domain.mybatis.payment;

import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentRepository;
import example.spring.hotel.domain.model.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    @Override
    public Optional<Payment> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    public void insert(Payment entity) {

    }
}
