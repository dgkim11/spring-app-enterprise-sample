package example.spring.hotel.domain.model.product;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.exception.EntityValidationException;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity 이름에 XXXEntity라는 이름을 붙인 경우는 DDD 에서 말하는 root entity의 경우에 해당한다. 즉, aggregate entity의 root entity만
 * 이렇게 이름을 정의한다. Entity에 대한 validation도 Entity 내부에서 해준다. 여기서는 spring의 validation 을 사용하였다.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements DomainEntity {
    private Long productId;

    private String productName;
    private long price;
    private LocalDateTime startSaleDateTime;
    private LocalDateTime endSaleDateTime;
    @Builder.Default
    private boolean validProduct = true;
    @Builder.Default
    private List<ProductOption> productOptions = new ArrayList<>();
    private String contents;
    @Builder.Default
    private boolean outOfStock = false;

    public void addProductOption(ProductOption option)  {
        if(productId == null) throw new EntityValidationException("productId가 null입니다.");
        option.setProductId(productId);
        productOptions.add(option);
    }

    public void invalateProduct() {
        validProduct = false;
    }

    /**
     * 현재 판매 가능한 상품인지 여부
     * @return
     */
    public boolean isProductSellable() {
        if(! validProduct || outOfStock) return false;
        if(endSaleDateTime != null) return LocalDateTime.now().compareTo(endSaleDateTime) < 0 ? true : false;
        if(startSaleDateTime != null) return LocalDateTime.now().compareTo(startSaleDateTime) > 0 ? true : false;

        return true;
    }
}
