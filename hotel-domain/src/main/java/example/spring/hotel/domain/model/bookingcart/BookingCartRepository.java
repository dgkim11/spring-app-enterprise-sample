package example.spring.hotel.domain.model.bookingcart;

import java.util.List;
import java.util.Optional;

public interface BookingCartRepository {
    Optional<BookingCart> findByCustomerId(Long customerId);
    void insertBookingCartItem(BookingCartItem bookingCartItem);
    void insertBookingCartItemOption(BookingCartItemOption bookingCartItemOption);
    void deleteBookingCartItemByCartItemId(Long cartItemId);
    void deleteBookingCartItemsByCustomerId(Long customerId);
}
