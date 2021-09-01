package example.spring.hotel.domain.product;

import example.spring.hotel.domain.DomainEntity;
import example.spring.hotel.domain.exception.EntityValidationException;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product implements DomainEntity {
    private Long productId;

    private @NotBlank String productName;
    private @Positive long price;
    private @NotNull Long categoryId;
    private @NotNull LocalDateTime startSaleDateTime;
    private LocalDateTime endSaleDateTime;
    private boolean validProduct = true;
    private List<ProductOption> productOptions = new ArrayList<>();
    private String contents;
    private @NotNull LocalDateTime createdAt;

    public void addProductOption(ProductOption option)  {
        if(productId == null) throw new EntityValidationException("productId가 null입니다.");
        productOptions.add(option);
    }
}
