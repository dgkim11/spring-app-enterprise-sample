package example.spring.hotel.infrastructure.domain.mybatis.payment;

import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.model.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private PaymentMapper paymentMapper;

    public PaymentRepositoryImpl(PaymentMapper paymentMapper)   {
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        PaymentRow paymentRow = paymentMapper.findPaymentById(paymentId);
        if(paymentRow == null) return Optional.empty();

        List<PaymentInfo> infos = paymentMapper.findPaymentInfosByPaymentId(paymentId);

        return Optional.of (Payment.builder()
                .paymentId(paymentId)
                .checkoutId(paymentRow.getCheckoutId())
                .totalPrice(paymentRow.getTotalPrice())
                .paidDateTime(paymentRow.getPaidDateTime())
                .paymentInfos(infos)
                .build());
    }

    @Override
    public int deleteById(Long paymentId) {
        return paymentMapper.deletePaymentById(paymentId);
    }

    @Override
    public void insert(Payment payment) {
        paymentMapper.insertPayment(payment);
        payment.getPaymentInfos().forEach(info -> paymentMapper.insertPaymentInfo(convertToPaymentInfoRow(payment, info)));
    }

    private PaymentInfoRow convertToPaymentInfoRow(Payment payment, PaymentInfo info) {
        return PaymentInfoRow.builder()
                .paymentId(payment.getPaymentId())
                .paymentType(info.getPaymentType())
                .price(info.getPrice())
                .accountNumber(info.getAccountNumber())
                .build();
    }

    @Override
    public void deleteByCustomerId(Long customerId) {
        paymentMapper.deletePaymentByCustomerId(customerId);
    }
}
