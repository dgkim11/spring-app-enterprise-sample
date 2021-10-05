package example.spring.hotel.domain.model.bookingcart;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.service.event.DomainEventConsumer;
import example.spring.hotel.domain.service.event.EventBroker;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@DependsOn("PaymentService")    // PaymentService에서 먼저 event channel을 생성해야 한다.
public class PaymentSuccessEventBookingCartConsumer implements DomainEventConsumer<PaymentSuccessEvent> {
    private CheckoutRepository checkoutRepository;
    private BookingCartRepository bookingCartRepository;
    private EventBroker eventBroker;
    private String id;

    public PaymentSuccessEventBookingCartConsumer(EventBroker eventBroker,
                                                  BookingCartRepository bookingCartRepository,
                                                  CheckoutRepository checkoutRepository) {
        this.bookingCartRepository = bookingCartRepository;
        this.checkoutRepository = checkoutRepository;
        this.eventBroker = eventBroker;

        subscribePaymentSuccessEvent(eventBroker);
    }
    private void subscribePaymentSuccessEvent(EventBroker eventBroker) {
        this.id = UUID.randomUUID().toString();
        try {
            eventBroker.subscribe(PaymentSuccessEvent.getEventKey(), this);
        } catch (NotExistEventChannelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void consume(PaymentSuccessEvent event) {
        removeBookingCartItemsByCheckoutId(event.getPayment().getCheckoutId());
    }

    private void removeBookingCartItemsByCheckoutId(Long checkoutId)    {
        Optional<Checkout> checkoutOptional = checkoutRepository.findById(checkoutId);
        if(checkoutOptional.isEmpty()) return;

        Checkout checkout = checkoutOptional.get();
        Optional<BookingCart> bookingCart = bookingCartRepository.findByCustomerId(checkout.getCustomerId());
        if(bookingCart.isEmpty()) return;

        deleteBookingCartItemsIfMatched(bookingCart.get(), checkout);
    }

    private void deleteBookingCartItemsIfMatched(BookingCart bookingCart, Checkout checkout) {
        List<BookingCartItem> bookingCartItems = bookingCart.getBookingCartItems();
        List<CheckoutItem> checkoutItems = checkout.getCheckoutItems();
        bookingCartItems.stream()
                .filter(item -> hasSameProductIdAndBookingDate(item, checkoutItems))
                .peek(this::removeCartItem)
                .count();
    }

    private void removeCartItem(BookingCartItem bookingCartItem) {
        bookingCartRepository.deleteBookingCartItemByCartItemId(bookingCartItem.getCartItemId());
    }

    private boolean hasSameProductIdAndBookingDate(BookingCartItem bookingCartItem, List<CheckoutItem> checkoutItems) {
        boolean result = checkoutItems.stream()
                .anyMatch(item ->
                            item.getProduct().getProductId().equals(bookingCartItem.getProduct().getProductId()) &&
                            item.getBookingDateTime().equals(bookingCartItem.getBookingDateTime()));
        return result;
    }
}
