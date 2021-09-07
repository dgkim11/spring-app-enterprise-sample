package example.spring.hotel.application.bookingcart

import example.spring.hotel.domain.model.bookingcart.BookingCartItem
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDateTime

@Title("예약 장바구니에 대한 비즈니스 Specification")
@Narrative("장바구니는 사용자기 구매하고자 하는 상품이 여러개가 있는 경우 장바구니에 담아서 한번에 주문할 수 있는 편리한 기능이다")
class BookingCartSpec extends Specification {

    def "장바구니에 상품을 담을 때 예약 날짜가 지정되어야 한다."() {
        BookingCartRepository bookingCartRepository = Stub(BookingCartRepository)
        BookingCartService bookingCartService = new BookingCartService(bookingCartRepository, Stub(ProductRepository))

        when: "예약 날짜 없이 장바구니에 상품 추가"
        bookingCartService.addToCart(1L, null, 1L)

        then: "장바구니에 상품을 담을 수 없다"
        thrown(AddToCartException)
    }
    def "같은 상품을 같은 날짜로 두개 이상 장바구니에 담지 못한다."()   {
        BookingCartRepository bookingCartRepository = Stub(BookingCartRepository)
        BookingCartService bookingCartService = new BookingCartService(bookingCartRepository, Stub(ProductRepository))

        given: "상품이 특정 날짜로 예약되어 장바구니에 있다."
        LocalDateTime bookingDateTime = LocalDateTime.now()
        bookingCartRepository.findByCustomerIdAndProductId(_,_) >> [ new BookingCartItem(bookingDateTime: bookingDateTime)]

        when: "동일 상품을 동일 날짜에 예약해서 장바구니에 추가한다."
        bookingCartService.addToCart(1L, bookingDateTime, 1L)

        then: "장바구니에 상품을 담을 수 없다"
        thrown(AddToCartException)
    }
    def "판매 가능 상품만 장바구니에 담을 수 있다."()    {
        BookingCartRepository bookingCartRepository = Stub(BookingCartRepository)
        ProductRepository productRepository = Stub(ProductRepository)
        BookingCartService bookingCartService = new BookingCartService(bookingCartRepository, productRepository)

        given: "판매 가능하지 않은 상품이 있다."
        productRepository.findById(_) >> Optional.of(new Product(outOfStock: true, validProduct: true)) // 품절 상품

        when: "판매 가능하지 않은 상품을 장바구니에 담는다."
        bookingCartService.addToCart(1L, LocalDateTime.now(), 1L)

        then: "장바구니에 상품을 담을 수 없다"
        thrown(AddToCartException)
    }
}
