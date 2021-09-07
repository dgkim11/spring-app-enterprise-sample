package example.spring.hotel.domain.model.bookingcart;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookingCart {
    private Long customerId;
    private List<BookingCartItem> products;
}
