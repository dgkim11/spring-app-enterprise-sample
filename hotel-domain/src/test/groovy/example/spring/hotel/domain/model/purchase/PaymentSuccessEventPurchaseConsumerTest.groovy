package example.spring.hotel.domain.model.purchase


import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutRepository
import example.spring.hotel.domain.model.payment.Payment
import example.spring.hotel.domain.model.payment.PaymentInfo
import example.spring.hotel.domain.model.payment.PaymentType
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.service.event.EventBroker
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent
import spock.lang.Specification

import java.time.LocalDateTime

class PaymentSuccessEventPurchaseConsumerTest extends Specification  {
    private PurchasedOrderRepository purchasedOrderRepository
    private CheckoutRepository checkoutRepository
    private EventBroker eventBroker

    def setup() {
        eventBroker = Stub(EventBroker)
        purchasedOrderRepository = Mock(PurchasedOrderRepository)
        checkoutRepository = Stub(CheckoutRepository)
    }

    def "결제 성공 이벤트가 발생하면 최종 구매 내역을 DB에 저장한다."()    {
        PaymentSuccessEventPurchaseConsumer consumer = new PaymentSuccessEventPurchaseConsumer(eventBroker, purchasedOrderRepository, checkoutRepository)

        given: "결제 성공 이벤트 발생"
        Checkout checkout = createCheckout()
        Payment payment = createPayment(checkout)
        PaymentSuccessEvent event = new PaymentSuccessEvent(payment)
        checkoutRepository.findById(_) >> Optional.of(checkout)

        when: "이벤트 소비"
        consumer.consume(event)

        then: "최종 구매 내역을 저장한다."
        1 * purchasedOrderRepository.insert({ purchasedOrder -> isEqual(purchasedOrder, checkout)})

    }

    private Checkout createCheckout()   {
        Long checkoutId = 1000L

        ObjectGraphBuilder builder = new ObjectGraphBuilder()
        builder.classNameResolver = "example.spring.hotel.domain.model.checkout"
        return builder.checkout(customerId: 1L, checkoutId: checkoutId)  {
            checkoutItem(checkoutId: checkoutId, product: new Product(productId:100L), bookingDateTime: LocalDateTime.now())
            checkoutItem(checkoutId: checkoutId, product: new Product(productId:101L), bookingDateTime: LocalDateTime.now())
            checkoutItem(checkoutId: checkoutId, product: new Product(productId:102L), bookingDateTime: LocalDateTime.now())
        }
    }

    private boolean isEqual(PurchasedOrder purchasedOrder, Checkout checkout)   {
        return purchasedOrder.getTotalPrice() == checkout.getTotalPrice() &&
                purchasedOrder.getPurchasedProducts().size() == checkout.getCheckoutItems().size() &&
                purchasedOrder.getPurchasedProducts().stream().allMatch(product -> existInCheckoutItems(product, checkout))
    }

    private boolean existInCheckoutItems(PurchasedProduct purchasedProduct, Checkout checkout) {
        return checkout.getCheckoutItems().stream().anyMatch(
                item -> item.getProduct().getProductId() == purchasedProduct.getProductId()).booleanValue()
    }

    private Payment createPayment(Checkout checkout) {
        return Payment.builder()
                .paymentId(100L)
                .totalPrice(checkout.getTotalPrice())
                .paymentInfos([new PaymentInfo(paymentType: PaymentType.BANK, price: 5000),
                               new PaymentInfo(paymentType: PaymentType.CARD, price: 5000)])
                .build()
    }
}
