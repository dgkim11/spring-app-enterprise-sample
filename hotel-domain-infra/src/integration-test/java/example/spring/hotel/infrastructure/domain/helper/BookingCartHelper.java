package example.spring.hotel.infrastructure.domain.helper;

import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.BookingCartItem;
import example.spring.hotel.domain.model.bookingcart.BookingCartItemOption;
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository;
import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException;
import example.spring.hotel.domain.model.product.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingCartHelper {
    private BookingCartRepository bookingCartRepository;

    private BookingCartHelper(BookingCartRepository bookingCartRepository)  {
        this.bookingCartRepository = bookingCartRepository;
    }

    public BookingCart createBookingCartWithOptions(Long customerId, Product product1, Product product2)   {
        BookingCart bookingCart = new BookingCart(customerId);

        try {
            BookingCartItem item1 = createBookingCartItem(customerId, product1);
            bookingCart.addBookingCartItem(item1);
            BookingCartItem item2 = createBookingCartItem(customerId, product2);
            bookingCart.addBookingCartItem(item2);
        } catch (AddToCartException e) {
            throw new RuntimeException(e);
        }


        return bookingCart;
    }

    public BookingCart createBookingCartWithoutOptions(Long customerId, Product product1, Product product2)   {
        BookingCart bookingCart = new BookingCart(customerId);

        try {
            // add booking cart item 1
            BookingCartItem item1 = new BookingCartItem(customerId,product1,LocalDateTime.now());
            bookingCartRepository.insertBookingCartItem(item1);
            bookingCart.addBookingCartItem(item1);

            // add booking cart item 2
            BookingCartItem item2 = new BookingCartItem(customerId, product2, LocalDateTime.now());
            bookingCartRepository.insertBookingCartItem(item2);
            bookingCart.addBookingCartItem(item2);

            return bookingCart;
        } catch (AddToCartException e) {
            throw new RuntimeException(e);
        }

    }

    public BookingCartItem createBookingCartItem(Long customerId, Product product)  {
        BookingCartItem item = new BookingCartItem(customerId, product, LocalDateTime.now());
        bookingCartRepository.insertBookingCartItem(item);
        item.addBookingCartItemOption(new BookingCartItemOption(item.getCartItemId(), product.getProductOptions().get(0).getOptionId()));
        bookingCartRepository.insertBookingCartItemOption(item.getItemOptions().get(0));

        return item;
    }
}
