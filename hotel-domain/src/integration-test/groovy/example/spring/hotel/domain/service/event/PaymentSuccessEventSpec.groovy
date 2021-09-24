package example.spring.hotel.domain.service.event

import example.spring.hotel.domain.bookingcart.BookingCartRepositoryTestImpl
import example.spring.hotel.domain.checkout.CheckoutRepositoryTestImpl
import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.config.IntegrationTestConfig
import example.spring.hotel.domain.model.bookingcart.BookingCart
import example.spring.hotel.domain.model.bookingcart.BookingCartItem
import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutItem
import example.spring.hotel.domain.model.payment.PaymentInfo
import example.spring.hotel.domain.model.payment.PaymentService
import example.spring.hotel.domain.model.payment.PaymentType
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.payment.PaymentGatewayAdapterSample1
import example.spring.hotel.domain.payment.PaymentRepositoryTestImpl
import example.spring.hotel.domain.purchase.PurchasedOrderRepositoryTestImpl
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelDomainConfig.class, IntegrationTestConfig.class])
class PaymentSuccessEventSpec extends Specification {
    @Autowired private PaymentService paymentService
    @Autowired private PaymentGatewayAdapterSample1 gateway1
    @Autowired private EventBroker eventBroker
    @Autowired private BookingCartRepositoryTestImpl bookingCartRepository
    @Autowired private CheckoutRepositoryTestImpl checkoutRepository
    @Autowired private PaymentRepositoryTestImpl paymentRepository
    @Autowired private PurchasedOrderRepositoryTestImpl purchaseRepository

    private Checkout checkout

    def setup() {
        Product product = createProduct()
        checkout = createCheckout(product)
        checkoutRepository.setCheckout(checkout)
        bookingCartRepository.setBookingCart(createBookingCart(product))
    }
    def "결제가 성공하면 PaymentSuccessEvent가 발생한다."() {
        given: "결제 이벤트를 구독한다."
        PaymentSuccessEventConsumer consumer = new PaymentSuccessEventConsumer(eventBroker)

        when: "결제를 수행한다."
        List<PaymentInfo> paymentInfoList = createPaymentInfoList()
        paymentService.pay(gateway1.companyId(), checkout, paymentInfoList)
        then: "이벤트를 받는다."
        consumer.getEvent() != null
    }

    def "PaymentSuccessEvent를 Payment, Purchase, BookingCart 도메인에서 consume한다."()   {
        when:"결제를 수행한다."
        List<PaymentInfo> paymentInfoList = createPaymentInfoList()
        paymentService.pay(gateway1.companyId(), checkout, paymentInfoList)

        then:"모든 도메인에서 event를 받는다."
        bookingCartRepository.isCalled()   // BookingCart
        checkoutRepository.isCalled()       // BookingCart
        paymentRepository.isInserted()  // Payment
        purchaseRepository.isInserted() // Purchase
    }

    private Product createProduct() {
        return new Product(productId:10000L, price:10000L)
    }
    private static BookingCart createBookingCart(Product product)   {
        return new BookingCart(1111L,
                [new BookingCartItem(cartItemId: 1L, customerId:1L, product:product, bookingDateTime: LocalDateTime.now())])
    }

    private static Checkout createCheckout(Product product)   {
        return new Checkout(checkoutId: 1000L,
                totalPrice: 10000L,
                checkoutItems: [new CheckoutItem(checkoutId: 1L, product:product, bookingDateTime: LocalDateTime.now())])
    }

    private static List<PaymentInfo> createPaymentInfoList()   {
        return [
            new PaymentInfo(PaymentType.CARD, 1000L, "1234"),
            new PaymentInfo(PaymentType.BANK, 9000L, "5678")
        ]
    }

    private static class PaymentSuccessEventConsumer implements DomainEventConsumer<PaymentSuccessEvent>    {
        private String id
        private EventBroker eventBroker
        private PaymentSuccessEvent event

        public PaymentSuccessEventConsumer(EventBroker eventBroker)    {
            this.id = UUID.randomUUID().toString()
            this.eventBroker = eventBroker
            subscribePaymentSuccessEvent()
        }
        @Override
        String getId() {
            return id
        }
        private void subscribePaymentSuccessEvent() {
            try {
                this.eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), this)
            } catch (NotExistEventChannelException e) {
                throw new RuntimeException(e)
            }
        }

        @Override
        void consume(PaymentSuccessEvent paymentEvent) {
            this.event = paymentEvent
        }

        public PaymentSuccessEvent getEvent()   {
            return this.event
        }
    }
}
