package example.spring.hotel.application.bookingcart

import example.spring.hotel.domain.model.bookingcart.BookingCart
import example.spring.hotel.domain.model.bookingcart.BookingCartItem
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import spock.lang.Specification

import java.time.LocalDateTime

class BookingCartServiceSpec extends Specification {
    def "장바구니가 비어있는 고객이 하나의 상품을 담는다."()    {
        BookingCartRepository cartRepository = Stub(BookingCartRepository)
        ProductRepository productRepository = Stub(ProductRepository)
        BookingCartService bookingCartService = new BookingCartService(cartRepository, productRepository)

        given: "장바구니가 비어있다."
        cartRepository.findByCustomerId(_) >> Optional.empty()      // 장바구니가 비어 있는 고객
        Product product = new Product(productId:1L)
        productRepository.findById(_) >> Optional.of(product)

        when: "장바구니에 상품을 넣는다."
        BookingCart bookingCart = bookingCartService.addToCart(1L, product.getProductId())

        then: "장바구니에 물건이 들어가 있다."
        bookingCart.getBookingCartItems().size() == 1
        bookingCart.getBookingCartItems().get(0).getProduct().getProductId() == product.getProductId()
    }
    def "이미 장바구니에 상품이 담긴 고객이 추가로 상품을 담는다."()    {
        BookingCartRepository cartRepository = Stub(BookingCartRepository)
        ProductRepository productRepository = Stub(ProductRepository)
        BookingCartService bookingCartService = new BookingCartService(cartRepository, productRepository)

        given: "장바구니에 상품이 담겨 있다."
        BookingCart cart = generateBookingCart()
        cartRepository.findByCustomerId(_) >> Optional.of(cart)
        Product product = new Product(productId:1000L)
        productRepository.findById(_) >> Optional.of(product)

        when: "추가로 상품을 담는다."
        bookingCartService.addToCart(1L, product.getProductId())

        then: "기존 상품과 추가한 상품이 장바구니에 있다."
        old(cart.getBookingCartItems().size()) == 1
        cart.getBookingCartItems().size() == 2
    }

    def "장바구니에 상품과 상품의 옵션을 함께 장바구니에 담는다."() {
        BookingCartRepository cartRepository = Stub(BookingCartRepository)
        ProductRepository productRepository = Stub(ProductRepository)
        BookingCartService bookingCartService = new BookingCartService(cartRepository, productRepository)

        given: "장바구니가 비어있다"
        cartRepository.findByCustomerId(_) >> Optional.empty()      // 장바구니가 비어 있는 고객
        Product product = new Product(productId:1L)
        productRepository.findById(_) >> Optional.of(product)

        when: "옵션과 함께 추가로 상품을 담는다."
        BookingCart cartAdded = bookingCartService.addToCart(1L, 2000L, [10L], LocalDateTime.now())

        then: "추가 상품의 옵션 정보도 함께 담는다."
        cartAdded.getBookingCartItems().size() == 1
        cartAdded.getBookingCartItems().get(0).getItemOptions().size() == 1
        cartAdded.getBookingCartItems().get(0).getItemOptions().get(0).productOptionId == 10L
    }

    private BookingCart generateBookingCart()   {
        return new BookingCart(1L,
                [new BookingCartItem(product: new Product(productId: 100L), bookingDateTime: LocalDateTime.now())])
    }
}
