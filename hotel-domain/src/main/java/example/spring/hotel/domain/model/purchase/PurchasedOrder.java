package example.spring.hotel.domain.model.purchase;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchasedOrder implements DomainEntity {
    private Long orderId;
    @Builder.Default
    private List<PurchasedProduct> orderItems = new LinkedList<>();
    private long totalPrice;
    private PaymentInfo paymentInfo;
    private LocalDateTime purchasedDateTime;
}
