package example.spring.hotel.domain.model.bookingcart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookingCartItemOption {
    private Long cartItemId;
    private Long productOptionId;
}
