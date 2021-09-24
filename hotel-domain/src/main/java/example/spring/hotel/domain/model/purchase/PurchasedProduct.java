package example.spring.hotel.domain.model.purchase;

import example.spring.hotel.domain.model.product.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedProduct {
    private Long productId;
    private List<Long> productOptionList;
}
