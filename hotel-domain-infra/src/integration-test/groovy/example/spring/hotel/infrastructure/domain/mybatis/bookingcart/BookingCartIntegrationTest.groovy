package example.spring.hotel.infrastructure.domain.mybatis.bookingcart

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.bookingcart.BookingCart
import example.spring.hotel.domain.model.bookingcart.BookingCartItem
import example.spring.hotel.domain.model.bookingcart.BookingCartRepository
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductOption
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.infrastructure.domain.config.HotelAppDomainInfraConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelAppDomainInfraConfig.class])
class BookingCartIntegrationTest extends Specification  {
    @Autowired BookingCartRepository repository
    @Autowired ProductRepository productRepository

    def "옵션 없는 상품들을 장바구니에 넣는다."()    {
        Long customerId = 1L
        Product product1 = createProduct("BookingCartIntegrationTest.productNoOption1")
        Product product2 = createProduct("BookingCartIntegrationTest.productNoOption2")

        given: "옵션 없는 상품 장바구니에 저장"
        BookingCart bookingCart = createBookingCartWithoutOptions(customerId, product1, product2)
        for(BookingCartItem cartItem : bookingCart.getBookingCartItems())   {
            repository.insertBookingCartItem(cartItem)
        }

        when: "해당 상품 조회"
        Optional<BookingCart> cartOptional = repository.findByCustomerId(bookingCart.getCustomerId())

        then: "상품이 존재한다."
        cartOptional.isPresent()
        cartOptional.get().getBookingCartItems().size() == bookingCart.getBookingCartItems().size()
        cartOptional.get().getBookingCartItems().get(0).getProduct().getProductId() == product1.getProductId()
        cartOptional.get().getBookingCartItems().get(1).getProduct().getProductId() == product2.getProductId()

        cleanup:
        repository.deleteBookingCartItemByCustomerId(customerId)
        deleteProduct(product1)
        deleteProduct(product2)
    }

    def "옵션 있는 상품들을 장바구니에 넣는다."()   {
        Long customerId = 1L
        Product product1 = createProduct("BookingCartIntegrationTest.product1")
        Product product2 = createProduct("BookingCartIntegrationTest.product2")

        given: "옵션 있는 상품 장바구니에 저장"
        BookingCart bookingCart = createBookingCartWithOptions(customerId, product1, product2)
        repository.insert(bookingCart)

        when: "해당 상품 조회"
        Optional<BookingCart> cartOptional = repository.findByCustomerId(bookingCart.getCustomerId())

        then: "상품이 존재한다."
        cartOptional.isPresent()
        cartOptional.get().getBookingCartItems().size() == bookingCart.getBookingCartItems().size()
        cartOptional.get().getBookingCartItems().get(0).getProduct().getProductId() == product1.getProductId()
        cartOptional.get().getBookingCartItems().get(0).getProduct().getProductOptions().size() == product1.getProductOptions().size()
        cartOptional.get().getBookingCartItems().get(1).getProduct().getProductId() == product2.getProductId()
        cartOptional.get().getBookingCartItems().get(1).getProduct().getProductOptions().size() == product2.getProductOptions().size()

        cleanup:
        repository.deleteBookingCartItemByCustomerId(customerId)
        deleteProduct(product1)
        deleteProduct(product2)
    }

    private BookingCart createBookingCartWithoutOptions(Long customerId, Product product1, Product product2)   {
        BookingCart bookingCart = new BookingCart(customerId)
        BookingCartItem item1 = new BookingCartItem(product: product1, bookingDateTime: LocalDateTime.now())
        BookingCartItem item2 = new BookingCartItem(product: product2, bookingDateTime: LocalDateTime.now())
        bookingCart.addBookingCartItem(item1)
        bookingCart.addBookingCartItem(item2)

        return bookingCart
    }
    private BookingCart createBookingCartWithOptions(Long customerId, Product product1, Product product2)   {
        BookingCart bookingCart = new BookingCart(customerId)
        BookingCartItem item1 = new BookingCartItem(product:product1, bookingDateTime: LocalDateTime.now())
        BookingCartItem item2 = new BookingCartItem(product:product2, bookingDateTime: LocalDateTime.now())
        bookingCart.addBookingCartItem(item1)
        bookingCart.addBookingCartItem(item2)

        return bookingCart
    }

    private Product createProduct(String productName) {
        Product product = new Product(productName:productName)
        productRepository.insert(product)
        product.addProductOption(new ProductOption(productId:product.getProductId(), optionName:"option1"))
        product.addProductOption(new ProductOption(productId:product.getProductId(), optionName:"option2"))
        productRepository.insertProductOptions(product.getProductOptions())

        return product
    }

    private void deleteProduct(Product product) {
        productRepository.deleteById(product.getProductId())
        productRepository.deleteProductOptionsByProductId(product.getProductId())
    }
}
