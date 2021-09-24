package example.spring.hotel.domain.model.bookingcart;

import example.spring.hotel.domain.model.DomainEntity;
import example.spring.hotel.domain.model.product.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class BookingCartItem implements DomainEntity  {
    private Long cartItemId;
    private @NonNull Long customerId;
    private @NonNull Product product;
    @Builder.Default
    private List<BookingCartItemOption> itemOptions = new ArrayList<>();
    private @NonNull LocalDateTime bookingDateTime;

    public void addBookingCartItemOption(BookingCartItemOption itemOption)  {
        itemOptions.add(itemOption);
    }

    protected void setCustomerId(Long customerId)   {
        this.customerId = customerId;
    }
}
