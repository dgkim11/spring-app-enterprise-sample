package example.spring.hotel.application.bookingcart;

import example.spring.hotel.domain.model.bookingcart.AddCartRequest;
import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.BookingCartItem;
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Validated
class BookingCartService {
    private BookingCartRepository bookingCartRepository;
    private ProductRepository productRepository;

    public BookingCartService(BookingCartRepository cartRepository, ProductRepository productRepository) {
        this.bookingCartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Optional<BookingCart> findBookingCartByCustomerId(Long customerId) {
        return bookingCartRepository.findByCustomerId(customerId);
    }

    public void addToCart(Long customerId, LocalDateTime bookingDateTime, Long productId) throws AddToCartException {
        validateBookingCartItem(customerId, bookingDateTime, productId);
        bookingCartRepository.addCartRequest(
                AddCartRequest.builder().customerId(customerId).productId(productId).bookingDateTime(bookingDateTime).build()
        );
    }

    public void addToCart(Long customerId, LocalDate bookingDate, Long productId, Long optionId) throws AddToCartException   {
        
    }
    public void addToCart(Long customerId, LocalDate bookingDate, Long productId, Long optionId1, Long optionId2) throws AddToCartException   {
        
    }
    public void addToCart(Long customerId, LocalDate bookingDate, Long productId, Long optionId1, Long optionId2, Long optionId3) throws AddToCartException    {
        
    }
    public void addToCart(Long customerId, LocalDate bookingDate, Long productId, Long ... optionId) throws AddToCartException  {
        
    }

    private void validateBookingCartItem(Long customerId, LocalDateTime bookingDateTime, Long productId) throws AddToCartException {
        if(bookingDateTime == null) throw new AddToCartException("bookingDate가 존재하지 않습니다.");
        if(hasSameProductAndBookingDateInCart(customerId, bookingDateTime, productId))
            throw new AddToCartException("동일한 상품이 동일한 예약 날짜로 이미 장바구니에 있습니다.");
        if(! isProductSellable(productId))
            throw new AddToCartException("해당 상품은 판매중인 상품이 아닙니다.");
    }

    /**
     * 현재 판매중인 상품인지 여부 확인
     * @param productId
     * @return
     */
    private boolean isProductSellable(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) return false;
        Product product = productOptional.get();
        return product.isProductSellable();
    }

    /**
     * 장바구니에 추가할 상품이 이미 동일한 예약 날짜로 장바구니에 존재하는지 확인.
     * @param customerId
     * @param bookingDateTime 장바구니에 담을 상품의 예약 날짜
     * @param productId 장바구니에 담을 상품 id
     * @return
     */
    private boolean hasSameProductAndBookingDateInCart(Long customerId, LocalDateTime bookingDateTime, Long productId)    {
        List<BookingCartItem> cartItems = bookingCartRepository.findByCustomerIdAndProductId(customerId, productId);
        for(BookingCartItem cartItem : cartItems)   {
            if(cartItem.getBookingDateTime().equals(bookingDateTime)) return true;
        }
        return false;
    }

}
