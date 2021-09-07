package example.spring.hotel.domain.infrastructure.mybatis.bookingcart;

import example.spring.hotel.domain.model.bookingcart.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookingCartRepositoryImpl implements BookingCartRepository {
    private BookingCartItemMapper mapper;

    public BookingCartRepositoryImpl(BookingCartItemMapper mapper)  {
        this.mapper = mapper;
    }

    public Optional<BookingCart> findByCustomerId(Long customerId)  {
        List<BookingCartItem> cartItems = mapper.findByCustomerId(customerId);
        if(cartItems.isEmpty()) return Optional.empty();
        List<BookingCartItem> cartItemsWithOptions = cartItems.stream()
                .peek(item -> addCartItemOptions(item))
                .collect(Collectors.toList());

        return Optional.of(new BookingCart(customerId, cartItemsWithOptions));
    }

    private BookingCartItem addCartItemOptions(BookingCartItem item) {
        List<BookingCartItemOption> itemOptions = mapper.findItemOptionsByCartItemId(item.getCartItemId());
        for(BookingCartItemOption itemOption : itemOptions) {
            item.addBookingCartItemOption(itemOption);
        }
        return item;
    }

    @Override
    public BookingCartItem addCartRequest(AddCartRequest cartRequest) {
        BookingCartItem cartItem = mapper.addBookingCartItem(cartRequest.getCustomerId(), cartRequest.getCustomerId(),cartRequest.getBookingDateTime());
        if(! cartRequest.getProductOptions().isEmpty())  {
            for(Long productOptionId: cartRequest.getProductOptions())  {
                mapper.addBookingCartProductOption(cartItem.getCartItemId(), productOptionId);
            }
        }
        return cartItem;
    }

    @Override
    public List<BookingCartItem> findByCustomerIdAndProductId(Long customerId, Long productId) {
        return mapper.findByCustomerIdAndProductId(customerId, productId);
    }

}
