package example.spring.hotel.domain.model.bookingcart

import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException
import example.spring.hotel.domain.model.product.Product
import spock.lang.Specification

import java.time.LocalDateTime

class BookingCartSpec extends Specification {
    def "판매 가능한 상품만 장바구니에 넣을 수 있다."() {
        Long customerId = 1L

        given: "판매가능하지 않은 상품 선택"
        Product product = new Product(outOfStock: true)     // 품절 상품.
        BookingCart bookingCart =  new BookingCart(customerId)

        when: "해당 상품 장바구니에 담기"
        bookingCart.addBookingCartItem(new BookingCartItem(product: product, bookingDateTime: LocalDateTime.now()))

        then: "장바구니 담기 실패"
        AddToCartException e = thrown()
        e.getMessage() == "해당 상품은 판매중인 상품이 아닙니다."
    }
    def "이미 동일상품에 대해 동일날짜로 예약이 존재하면 장바구니에 넣을 수 없다."()   {
        BookingCart bookingCart =  new BookingCart(1L)

        given: "장바구니에 상품이 담겨 있다."
        Product product = new Product(productId: 1L, outOfStock: false, validProduct: true)
        BookingCartItem cartItem = new BookingCartItem(product: product, bookingDateTime: LocalDateTime.now())
        bookingCart.addBookingCartItem(cartItem)

        when: "동일상품에 대해 동일 날짜로 장바구니 담기"
        bookingCart.addBookingCartItem(cartItem)

        then: "장바구니 담기 실패"
        AddToCartException e = thrown()
        e.getMessage() == "동일한 상품이 동일한 예약 날짜로 이미 장바구니에 있습니다."
    }
    def "예약 날짜가 존재하지 않으면 장바구니에 담을 수 없다."()  {
        BookingCart bookingCart =  new BookingCart(1L)

        given: "예약 날짜 선택 없이 상품 선택"
        Product product = new Product(productId: 1L, outOfStock: false, validProduct: true)
        BookingCartItem cartItem = new BookingCartItem(product: product, bookingDateTime: null)

        when: "장바구니에 담기"
        bookingCart.addBookingCartItem(cartItem)

        then: "장바구니 담기 실패"
        AddToCartException e = thrown()
        e.getMessage() == "bookingDate가 존재하지 않습니다."
    }
}
