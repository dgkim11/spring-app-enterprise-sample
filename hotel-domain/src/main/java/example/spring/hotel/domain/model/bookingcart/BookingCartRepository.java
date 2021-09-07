package example.spring.hotel.domain.model.bookingcart;

import java.util.List;
import java.util.Optional;

public interface BookingCartRepository {
    Optional<BookingCart> findByCustomerId(Long customerId);
    BookingCartItem addCartRequest(AddCartRequest addCartRequest);

    List<BookingCartItem> findByCustomerIdAndProductId(Long customerId, Long productId);
}
