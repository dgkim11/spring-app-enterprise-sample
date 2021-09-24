package example.spring.hotel.domain.bookingcart;

import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.BookingCartItem;
import example.spring.hotel.domain.model.bookingcart.BookingCartItemOption;
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BookingCartRepositoryTestImpl implements BookingCartRepository {
    private BookingCart bookingCart;
    private boolean called;

    public void setBookingCart(BookingCart bookingCart) {
        this.bookingCart = bookingCart;
    }
    public boolean isCalled()   { return this.called; }

    @Override
    public Optional<BookingCart> findByCustomerId(Long customerId) {
        this.called = true;
        return Optional.of(bookingCart);
    }

    @Override
    public void insertBookingCartItem(BookingCartItem bookingCartItem) {

    }

    @Override
    public void insertBookingCartItemOption(BookingCartItemOption bookingCartItemOption) {

    }

    @Override
    public void deleteBookingCartItemByCartItemId(Long cartItemId) {

    }

    @Override
    public void deleteBookingCartItemsByCustomerId(Long customerId) {

    }
}
