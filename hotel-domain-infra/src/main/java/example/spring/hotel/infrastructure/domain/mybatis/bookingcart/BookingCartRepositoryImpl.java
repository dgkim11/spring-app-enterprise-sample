package example.spring.hotel.infrastructure.domain.mybatis.bookingcart;

import example.spring.hotel.domain.model.bookingcart.*;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Repository;

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

    @Override
    public List<BookingCartItem> findByCustomerIdAndProductId(Long customerId, Long productId) {
        return mapper.findByCustomerIdAndProductId(customerId, productId);
    }

    public void insertBookingCartItem(BookingCartItem item) {
        mapper.insertBookingCartItem(item);
    }

    public void insertBookingCartItemOption(BookingCartItemOption option)   {
        mapper.deleteBookingCartItemOptionByCartItemId(option.getCartItemId());
        mapper.insertBookingCartProductOption(option);
    }

    @Override
    public void deleteBookingCartItemByCartItemId(Long cartItemId) {
        mapper.deleteBookingCartItemOptionByCartItemId(cartItemId);
    }

    @Override
    public void deleteBookingCartItemByCustomerId(Long customerId) {
        mapper.deleteBookingCartItemByCustomerId(customerId);
    }

}
