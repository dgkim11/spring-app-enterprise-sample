package example.spring.hotel.infrastructure.domain.mybatis.bookingcart

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.bookingcart.BookingCart
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.infrastructure.domain.config.HotelDomainInfraConfig
import example.spring.hotel.infrastructure.domain.config.IntegrationTestConfig
import example.spring.hotel.infrastructure.domain.helper.BookingCartHelper
import example.spring.hotel.infrastructure.domain.helper.ProductHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelDomainInfraConfig.class, IntegrationTestConfig.class ])
class BookingCartIntegrationTest extends Specification  {
    @Autowired private BookingCartRepository bookingCartRepository
    @Autowired private ProductRepository productRepository
    @Autowired private ProductHelper productHelper
    @Autowired private BookingCartHelper bookingCartHelper

    private Long customerId = 1L
    private Product product1
    private Product product2

    def setup() {
        new Product(productId: 1L, productName: "fgf")

        String productName1 = "BookingCartIntegrationTest.productNoOption1";
        String productName2 = "BookingCartIntegrationTest.productNoOption2";
        productHelper.deleteProductByName(productName1)
        productHelper.deleteProductByName(productName2)
        product1 = productHelper.createProduct(productName1, 1000L, 3)
        product2 = productHelper.createProduct(productName2, 1000L, 3)
    }

    def cleanup()   {
        bookingCartRepository.deleteBookingCartItemsByCustomerId(customerId)
        deleteProduct(product1)
        deleteProduct(product2)
    }

    def "옵션 없는 상품들을 장바구니에 넣는다."()    {
        given: "옵션 없는 상품 장바구니에 저장"
        BookingCart bookingCart = bookingCartHelper.createBookingCartWithoutOptions(customerId, product1, product2)

        when: "해당 상품 조회"
        Optional<BookingCart> cartOptional = bookingCartRepository.findByCustomerId(bookingCart.getCustomerId())

        then: "상품이 존재한다."
        cartOptional.isPresent()
        cartOptional.get().getBookingCartItems().size() == bookingCart.getBookingCartItems().size()
        cartOptional.get().getBookingCartItems().get(0).getProduct().getProductId() == product1.getProductId()
        cartOptional.get().getBookingCartItems().get(1).getProduct().getProductId() == product2.getProductId()
    }

    def "옵션 있는 상품들을 장바구니에 넣는다."()   {
        given: "옵션 있는 상품 장바구니에 저장"
        BookingCart bookingCart = createBookingCartWithOptions(customerId, product1, product2)

        when: "해당 상품 조회"
        Optional<BookingCart> cartOptional = bookingCartRepository.findByCustomerId(bookingCart.getCustomerId())

        then: "상품이 존재한다."
        cartOptional.isPresent()
        cartOptional.get().getBookingCartItems().size() == bookingCart.getBookingCartItems().size()
        cartOptional.get().getBookingCartItems().get(0).getProduct().getProductId() == product1.getProductId()
        cartOptional.get().getBookingCartItems().get(0).getItemOptions().size() == bookingCart.getBookingCartItems().get(0).getItemOptions().size()
        cartOptional.get().getBookingCartItems().get(1).getProduct().getProductId() == product2.getProductId()
        cartOptional.get().getBookingCartItems().get(1).getItemOptions().size() == bookingCart.getBookingCartItems().get(1).getItemOptions().size()
    }

    private BookingCart createBookingCartWithOptions(Long customerId, Product product1, Product product2)   {
        bookingCartRepository.deleteBookingCartItemsByCustomerId(customerId)
        bookingCartHelper.createBookingCartWithOptions(customerId, product1, product2)
    }

    private void deleteProduct(Product product) {
        productRepository.deleteById(product.getProductId())
    }
}
