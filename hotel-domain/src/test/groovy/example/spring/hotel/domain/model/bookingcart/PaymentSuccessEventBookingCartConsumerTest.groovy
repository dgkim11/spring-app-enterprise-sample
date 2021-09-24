package example.spring.hotel.domain.model.bookingcart

import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutRepository
import example.spring.hotel.domain.model.payment.Payment
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository
import example.spring.hotel.domain.service.event.EventBroker
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent
import spock.lang.Specification

import java.time.LocalDateTime

class PaymentSuccessEventBookingCartConsumerTest extends Specification  {
    private EventBroker eventBroker
    private PurchasedOrderRepository purchaseRepository
    private BookingCartRepository bookingCartRepository
    private CheckoutRepository checkoutRepository
    Long customerId = 1L

    def setup() {
        eventBroker = Stub(EventBroker)
        purchaseRepository = Stub(PurchasedOrderRepository)
        bookingCartRepository = Stub(BookingCartRepository)
        checkoutRepository = Stub(CheckoutRepository)
    }

    def "결제 성공 시 결제된 상품들은 장바구니에서 삭제한다."()   {
        PaymentSuccessEventBookingCartConsumer consumer =
                new PaymentSuccessEventBookingCartConsumer(eventBroker, bookingCartRepository, checkoutRepository)

        given: "장바구니에 상품 적재"
        BookingCart bookingCart = createBookingCart()
        Checkout checkout = createCheckout(bookingCart)
        checkoutRepository.findById(_) >> Optional.of(checkout)
        bookingCartRepository.findByCustomerId(_) >> Optional.of(bookingCart)
        Payment payment = createPayment(checkout)

        and: "상품 결제"
        PaymentSuccessEvent event = new PaymentSuccessEvent(payment)

        when: "장바구니에서 삭제"
        consumer.consume(event)

        then: "결제 상품들은 장바구니에서 삭제됨"

    }

    private BookingCart createBookingCart() {
        ObjectGraphBuilder builder = new ObjectGraphBuilder()
        builder.classNameResolver = "example.spring.hotel.domain.model.bookingcart"
        return builder.bookingCart(customerId: customerId) {
                bookingCartItem(cartItemId: 100L,
                        customerId:customerId,
                        product: new Product(productId:100L),
                        bookingDateTime: LocalDateTime.now())
                bookingCartItem(cartItemId: 100L,
                        customerId:customerId,
                        product: new Product(productId:100L),
                        bookingDateTime: LocalDateTime.now())
                bookingCartItem(cartItemId: 100L,
                        customerId:customerId,
                        product: new Product(productId:100L),
                        bookingDateTime: LocalDateTime.now())
        }
    }

    private Checkout createCheckout(BookingCart bookingCart)   {
        Long checkoutId = 1000L

        ObjectGraphBuilder builder = new ObjectGraphBuilder()
        builder.classNameResolver = "example.spring.hotel.domain.model.checkout"
        List<BookingCartItem> cartItems = bookingCart.getBookingCartItems();
        return builder.checkout(customerId: customerId, checkoutId: checkoutId)  {
            checkoutItem(checkoutId: checkoutId, product: cartItems.get(0).getProduct(), bookingDateTime:  cartItems.get(0).getBookingDateTime())
            checkoutItem(checkoutId: checkoutId, product: cartItems.get(1).getProduct(), bookingDateTime:  cartItems.get(1).getBookingDateTime())
            checkoutItem(checkoutId: checkoutId, product: cartItems.get(2).getProduct(), bookingDateTime:  cartItems.get(2).getBookingDateTime())
        }
    }

    private Payment createPayment(Checkout checkout)    {
        return new Payment(paymentId:1234L, checkoutId:checkout.getCheckoutId())
    }
}
