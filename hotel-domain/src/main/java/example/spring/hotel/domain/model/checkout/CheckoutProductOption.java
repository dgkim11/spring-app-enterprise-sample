package example.spring.hotel.domain.model.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutProductOption {
    private Long productOptionId;
    private long optionPrice;       // snapshot. checkout 시점에 price를 저장한다. 이후 가격이 바뀌더라도 고객은 이 가격으로 주문한다.
}
