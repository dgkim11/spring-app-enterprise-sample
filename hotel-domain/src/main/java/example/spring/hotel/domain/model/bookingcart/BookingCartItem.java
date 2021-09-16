package example.spring.hotel.domain.model.bookingcart;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.product.Product;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingCartItem implements DomainEntity  {
    private Long cartItemId;
    private @NotNull Long customerId;
    private @NotNull Product product;
    @Builder.Default
    private List<BookingCartItemOption> productOptions = new ArrayList<>();
    private @NotNull LocalDateTime bookingDateTime;

    public void addBookingCartItemOption(BookingCartItemOption itemOption)  {
        productOptions.add(itemOption);
    }

    protected void setCustomerId(Long customerId)   {
        this.customerId = customerId;
    }
}
