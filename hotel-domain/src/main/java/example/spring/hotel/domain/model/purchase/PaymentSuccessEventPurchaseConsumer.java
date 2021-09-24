package example.spring.hotel.domain.model.purchase;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import example.spring.hotel.domain.service.event.DomainEventConsumer;
import example.spring.hotel.domain.service.event.EventBroker;
import example.spring.hotel.domain.service.event.exception.NotExistEventChannelException;
import example.spring.hotel.domain.service.event.payment.PaymentSuccessEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CQRS 패턴의 예이다. PaymentSuccessEvent를 받아서 최종 주문 내역 정보를 Payment와 Checkout 도메인으로부터 얻어서
 * Purchase 도메인에 적합한 형태로 데이터를 저장한다.
 */
@Service
@DependsOn("PaymentService")    // PaymentService에서 먼저 event channel을 생성해야 한다.
public class PaymentSuccessEventPurchaseConsumer implements DomainEventConsumer<PaymentSuccessEvent> {
    private PurchasedOrderRepository purchasedOrderRepository;
    private CheckoutRepository checkoutRepository;
    private EventBroker eventBroker;
    private String id;

    public PaymentSuccessEventPurchaseConsumer(EventBroker eventBroker,
                                               PurchasedOrderRepository purchasedOrderRepository,
                                               CheckoutRepository checkoutRepository) {
        this.purchasedOrderRepository = purchasedOrderRepository;
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
        Payment payment = event.getPayment();
        PurchasedOrder order = PurchasedOrder.builder()
                .paymentInfoList(getPaymentInfoList(payment))
                .purchasedDateTime(payment.getPaidDateTime())
                .totalPrice(payment.getTotalPrice())
                .purchasedProducts(getOrderItems(payment))
                .build();

        purchasedOrderRepository.insert(order);
    }

    private List<PurchasedProduct> getOrderItems(Payment payment) {
        Optional<Checkout> checkoutOptional = checkoutRepository.findById(payment.getCheckoutId());
        if(checkoutOptional.isEmpty()) throw new RuntimeException("check 정보가 없다. checkoutId:" + payment.getCheckoutId());
        Checkout checkout = checkoutOptional.get();
        List<CheckoutItem> checkoutItems = checkout.getCheckoutItems();
        return checkoutItems.stream().map(item -> toPurchasedProduct(item)).collect(Collectors.toList());
    }

    private PurchasedProduct toPurchasedProduct(CheckoutItem item) {
        List<Long> options =
                item.getCheckoutProductOptions().stream()
                .map(option -> option.getProductOptionId())
                .collect(Collectors.toList());

        return new PurchasedProduct(item.getProduct().getProductId(), options);
    }

    private List<PaymentInfo> getPaymentInfoList(Payment payment) {
        return payment.getPaymentInfos().stream()
                .map(p -> new PaymentInfo(p.getPaymentType(), p.getPrice(), p.getAccountNumber()))
                .collect(Collectors.toList());
    }
}
