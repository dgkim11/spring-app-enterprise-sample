package example.spring.hotel.infrastructure.domain.mybatis.purchase;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * PurchasedOrder가 실질적인 엔티티이지만 DB에 저장될 때는 모든 정보를 snapshot형태로 String 타입으로 serialize해서 저장한다.
 */
@Getter
@RequiredArgsConstructor
public class PurchasedOrderRow {
    private Long purchasedOrderId;
    @NonNull
    private Long customerId;
    @NonNull
    private String purchasedOrderSnapshot;
}
