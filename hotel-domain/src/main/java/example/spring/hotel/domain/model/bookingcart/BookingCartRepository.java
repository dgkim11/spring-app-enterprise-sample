package example.spring.hotel.domain.model.bookingcart;

import java.util.List;
import java.util.Optional;

public interface BookingCartRepository {
    Optional<BookingCart> findByCustomerId(Long customerId);
    List<BookingCartItem> findByCustomerIdAndProductId(Long customerId, Long productId);
    void insertBookingCartItem(BookingCartItem bookingCart);
    void insertBookingCartItemOption(BookingCartItemOption bookingCartItemOption);
    void deleteBookingCartItemByCartItemId(Long cartItemId);
    void deleteBookingCartItemByCustomerId(Long customerId);
}
