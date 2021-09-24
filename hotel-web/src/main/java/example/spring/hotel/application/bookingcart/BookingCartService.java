package example.spring.hotel.application.bookingcart;

import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.BookingCartItem;
import example.spring.hotel.domain.model.bookingcart.BookingCartItemOption;
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository;
import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookingCartService {
    private BookingCartRepository bookingCartRepository;
    private ProductRepository productRepository;

    public BookingCartService(BookingCartRepository cartRepository, ProductRepository productRepository) {
        this.bookingCartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Optional<BookingCart> findBookingCartByCustomerId(Long customerId) {
        return bookingCartRepository.findByCustomerId(customerId);
    }

    @Transactional
    public BookingCart addToCart(Long customerId, Long productId, LocalDateTime bookingDateTime) throws AddToCartException {
        BookingCart bookingCart = generateBookingCart(customerId);
        BookingCartItem bookingCartItem = BookingCartItem.builder()
                .bookingDateTime(bookingDateTime)
                .customerId(customerId)
                .product(productRepository.findById(productId).get())
                .build();

        bookingCartRepository.insertBookingCartItem(bookingCartItem);
        bookingCart.addBookingCartItem(bookingCartItem);

        return bookingCart;
    }

    @Transactional
    public BookingCart addToCart(Long customerId, Long productId, List<Long> optionIds, LocalDateTime bookingDateTime) throws AddToCartException   {
        BookingCart bookingCart = generateBookingCart(customerId);
        BookingCartItem cartItem = BookingCartItem.builder()
                .bookingDateTime(bookingDateTime)
                .customerId(customerId)
                .product(productRepository.findById(productId).get())
                .build();

        // bookingCartItem을 먼저 저장해야 cartItemId가 부여가 되기때문에 아래의 순서로 저장한다.
        bookingCartRepository.insertBookingCartItem(cartItem);
        for(Long optionId : optionIds)  {
            BookingCartItemOption itemOption = new BookingCartItemOption(cartItem.getCartItemId(), optionId);
            bookingCartRepository.insertBookingCartItemOption(itemOption);
            cartItem.addBookingCartItemOption(itemOption);
        }

        bookingCart.addBookingCartItem(cartItem);

        return bookingCart;
    }

    private BookingCart generateBookingCart(Long customerId)    {
        BookingCart bookingCart;
        Optional<BookingCart> bookingCartOptional = bookingCartRepository.findByCustomerId(customerId);
        if(bookingCartOptional.isEmpty())   {
            bookingCart = new BookingCart(customerId);
        }
        else    {
            bookingCart = bookingCartOptional.get();
        }

        return bookingCart;
    }
}
