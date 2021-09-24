package example.spring.hotel.domain.model.payment

import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutItem
import example.spring.hotel.domain.model.payment.exception.PaymentException
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.service.event.EventBroker
import spock.lang.Specification

class PaymentServiceSpec extends Specification {
    PaymentGatewayAdapter paymentGatewayAdapter = new TestPaymentGatewayAdapter()
    EventBroker eventBroker = Stub(EventBroker)
    PaymentService paymentService

    def setup() {
        paymentService = new PaymentService([paymentGatewayAdapter], eventBroker)
        paymentGatewayAdapter.execute(_,_) >> new PaymentResult(true, null)
    }

    def "checkout item의 총 결제금액과 결재 타입별 결제금액의 총합이 같아야 한다."() {

        given: "checkout item 생성"
        CheckoutItem checkoutItem1 = new CheckoutItem(product: new Product(price: 1000L))
        CheckoutItem checkoutItem2 = new CheckoutItem(product: new Product(price: 2000L))
        Checkout checkout = new Checkout(checkoutItems: [checkoutItem1, checkoutItem2], totalPrice:3000L)

        and: "결제 타입별 결재금액 정의"
        PaymentInfo paymentInfo1 = new PaymentInfo(paymentType: PaymentType.BANK, price: 1500)
        PaymentInfo paymentInfo2 = new PaymentInfo(paymentType: PaymentType.CARD, price: 1000)

        when: "결재 수행"
        paymentService.pay(paymentGatewayAdapter.companyId(), checkout, [paymentInfo1, paymentInfo2])

        then:
        PaymentException e = thrown()
        e.getMessage() == "총 금액이 맞지 않습니다."
    }

    def "하나의 결제에 동일한 결제타입이 두개 이상 존재할 수 없다."()    {
        given: "동일한 결제 타입이 2개 존재한다."
        CheckoutItem checkoutItem1 = new CheckoutItem(product: new Product(price: 1000L))
        CheckoutItem checkoutItem2 = new CheckoutItem(product: new Product(price: 2000L))
        Checkout checkout = new Checkout(checkoutItems: [checkoutItem1, checkoutItem2], totalPrice:3000L)
        PaymentInfo paymentInfo1 = new PaymentInfo(paymentType: PaymentType.BANK, price: 1500)
        PaymentInfo paymentInfo2 = new PaymentInfo(paymentType: PaymentType.BANK, price: 1000)

        when: "결제를 수행한다."
        paymentService.pay(paymentGatewayAdapter.companyId(), checkout, [paymentInfo1, paymentInfo2])

        then: "결제 실패"
        PaymentException e = thrown()
        e.getMessage() == "동일한 결제타입이 여러개 존재합니다."
    }

    private static class TestPaymentGatewayAdapter implements PaymentGatewayAdapter {

        @Override
        String companyId() {
            return "testPayment"
        }

        @Override
        PaymentResult execute(long totalPrice, List<PaymentInfo> paymentInfoList) {
            return new PaymentResult(true, "")
        }
    }
}
