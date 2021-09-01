package example.spring.hotel.domain.product;

import lombok.*;

/**
 * 호텔 예약 시 옵션을 선택할 수 있다. 예로,조식/석시 여부, 흡연여부 등등이 옵션이 될 수 있다.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {
    private String optionId;
    private Long productId;
    private String optionName;
    private String description;

    /**
     * 각각의 옵션은 별도의 추가 요금이 발생할 수 있다. 없는 경우는 0 이다.
     */
    private long price;
}
