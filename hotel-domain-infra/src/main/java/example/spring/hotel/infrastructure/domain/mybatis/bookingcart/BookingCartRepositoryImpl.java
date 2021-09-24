package example.spring.hotel.infrastructure.domain.mybatis.bookingcart;

import example.spring.hotel.domain.model.bookingcart.*;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookingCartRepositoryImpl implements BookingCartRepository {
    private BookingCartItemMapper mapper;
    private ProductRepository productRepository;

    public BookingCartRepositoryImpl(BookingCartItemMapper mapper, ProductRepository productRepository)  {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    public Optional<BookingCart> findByCustomerId(Long customerId)  {
        List<BookingCartItemRow> cartItems = mapper.findByCustomerId(customerId);
        if(cartItems.isEmpty()) return Optional.empty();
        List<BookingCartItem> cartItemsWithOptions = cartItems.stream()
                .map(this::convertToBookingCartItem)
                .peek(this::addCartItemOptions)
                .collect(Collectors.toList());

        return Optional.of(new BookingCart(customerId, cartItemsWithOptions));
    }

    private BookingCartItem convertToBookingCartItem(BookingCartItemRow row)  {
        return BookingCartItem.builder()
                .cartItemId(row.getCartItemId())
                .bookingDateTime(row.getBookingDateTime())
                .customerId(row.getCustomerId())
                .product(findProduct(row.getProductId()))
                .build();
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).get();
    }

    private BookingCartItem addCartItemOptions(BookingCartItem item) {
        List<BookingCartItemOption> itemOptions = mapper.findItemOptionsByCartItemId(item.getCartItemId());
        for(BookingCartItemOption itemOption : itemOptions) {
            item.addBookingCartItemOption(itemOption);
        }
        return item;
    }

    public void insertBookingCartItem(BookingCartItem bookingCartItem) {
        mapper.insertBookingCartItem(bookingCartItem);
    }

    public void insertBookingCartItemOption(BookingCartItemOption option)   {
        mapper.deleteBookingCartItemOptionsByCartItemId(option.getCartItemId());
        mapper.insertBookingCartItemOption(option);
    }

    @Override
    @Transactional
    public void deleteBookingCartItemByCartItemId(Long cartItemId) {
        mapper.deleteBookingCartItemOptionsByCartItemId(cartItemId);
        mapper.deleteBookingCartItemByCartItemId(cartItemId);
    }

    @Override
    public void deleteBookingCartItemsByCustomerId(Long customerId) {
        mapper.deleteBookingCartItemOptionsByCustomerId(customerId);
        mapper.deleteBookingCartItemsByCustomerId(customerId);
    }
}
