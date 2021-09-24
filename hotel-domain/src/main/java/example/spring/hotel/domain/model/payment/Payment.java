package example.spring.hotel.domain.model.payment;

import example.spring.hotel.domain.model.DomainEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements DomainEntity {
    private Long paymentId;
    private Long customerId;
    private Long checkoutId;
    @Builder.Default
    private List<PaymentInfo> paymentInfos = new LinkedList<>();
    private LocalDateTime paidDateTime;
    private long totalPrice;
}
