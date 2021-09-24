package example.spring.hotel.infrastructure.domain.mybatis.payment

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.payment.Payment
import example.spring.hotel.domain.model.payment.PaymentInfo
import example.spring.hotel.domain.model.payment.PaymentRepository
import example.spring.hotel.domain.model.payment.PaymentType
import example.spring.hotel.infrastructure.domain.config.HotelDomainInfraConfig
import example.spring.hotel.infrastructure.domain.config.IntegrationTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelDomainInfraConfig.class, IntegrationTestConfig.class ])
class PaymentIntegrationTest extends Specification {
    @Autowired private PaymentRepository paymentRepository
    private Payment payment
    private Long customerId = 1L

    def cleanup()   {
        if(payment != null)
            paymentRepository.deleteByCustomerId(customerId)
    }

    def "payment 데이타가 올바로 저장되고 조회된다."() {
        given:"payment 데이터 저장"
        payment = createPayment()

        when: "payment 데이터 조회"
        Optional<Payment> paymentOptional = paymentRepository.findById(payment.getPaymentId())

        then: "저장시의 데이타와 조회된 데이터가 동일해야 한다."
        paymentOptional.isPresent()
        paymentOptional.get().getCheckoutId() == payment.getCheckoutId()
        paymentOptional.get().getTotalPrice() == payment.getTotalPrice()
        paymentOptional.get().getPaymentInfos().size() == payment.getPaymentInfos().size()
        paymentOptional.get().getPaymentInfos().get(0).getPaymentType() == payment.getPaymentInfos().get(0).getPaymentType()
    }

    def "payment 데이터가 삭제된다."()  {
        given:"payment 데이터 생성"
        payment = createPayment()

        when: "데이터 삭제"
        paymentRepository.deleteById(payment.getPaymentId())

        then: "해당 payment 데이터 조회 안됨"
        paymentRepository.findById(payment.getPaymentId()).isEmpty()
    }

    private Payment createPayment()    {
        Payment payment = new Payment(
                checkoutId: 1L,
                customerId: customerId,
                paidDateTime: LocalDateTime.now(),
                paymentInfos: [new PaymentInfo(PaymentType.CARD, 10000L, "1234"),
                               new PaymentInfo(PaymentType.BANK, 20000L, "5678")],
                totalPrice:30000L
        )

        paymentRepository.insert(payment)

        return payment

    }
}
