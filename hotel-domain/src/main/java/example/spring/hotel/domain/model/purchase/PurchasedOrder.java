package example.spring.hotel.domain.model.purchase;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedOrder implements DomainEntity {
    private Long purchasedOrderId;
    private Long customerId;
    @Builder.Default
    private List<PurchasedProduct> purchasedProducts = new LinkedList<>();
    private long totalPrice;
    @Builder.Default
    private List<PaymentInfo> paymentInfoList = new LinkedList<>();
    private LocalDateTime purchasedDateTime;
}
