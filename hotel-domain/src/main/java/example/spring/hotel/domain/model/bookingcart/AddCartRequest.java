package example.spring.hotel.domain.model.bookingcart;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddCartRequest  {
    @NotNull
    private Long customerId;
    @NotNull
    private Long productId;
    @Builder.Default
    private List<Long> productOptions = new ArrayList<>();
    @NotNull
    private LocalDateTime bookingDateTime;
}
