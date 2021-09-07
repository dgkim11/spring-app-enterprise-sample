package example.spring.hotel.domain.model.product;

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

    /**
     * NOTE: 접근자가 protected이다. setXXX()는 외부에 public으로 노출해서는 안된다. 그러나, Product 객체가
     * ProductOption 객체를 소유하는 root entity로서 ProductOption 객체는 Product의 하위 객체라 볼 수 있다.
     * 따라서, Product에게는 자신의 field를 수정할 수 있는 메쏘드를 제공할 수 있다. 단, 꼭 필요한 경우에만 제한한다.
     * 여기서는 최초 option 정보를 만들 때 상품도 아직 DB에 들어간 상태가 아니기 때문에 초기에 productId가 null이다.
     * 상품이 dB에 들어가면 productId가 생성되며 그 이후 option에 대한 productId를 해당 메서드로 부여한다.
     * @param productId
     */
    protected void setProductId(Long productId) {
        this.productId = productId;
    }
}
