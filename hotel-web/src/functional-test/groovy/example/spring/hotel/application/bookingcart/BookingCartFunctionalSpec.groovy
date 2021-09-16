package example.spring.hotel.application.bookingcart

import example.spring.hotel.application.customer.CustomerControlService
import example.spring.hotel.application.product.ProductManager
import example.spring.hotel.domain.model.bookingcart.BookingCart
import example.spring.hotel.domain.model.customer.Customer
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductOption
import example.spring.hotel.web.config.HotelApplicationWebConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelApplicationWebConfig.class])
class BookingCartFunctionalSpec extends Specification  {
    @Autowired private BookingCartService bookingCartService
    @Autowired private CustomerControlService customerControlService
    @Autowired private ProductManager productManager

    private Customer customer
    private Product productWithoutOptions
    private Product productWithOptions

    def setup() {
        createCustomerAndProducts()
    }

    def "옵션 없는 상품을 장바구니에 저장 후 장바구니 조회 시 옵션이 없어야 한다."()   {
        given: "고객은 옵션 없는 상품을 장바구니에 저장한다."
        bookingCartService.addToCart(customer.getCustomerId(), LocalDateTime.now(), productWithoutOptions.getProductId())

        when: "고객의 장바구니를 조회한다."
        Optional<BookingCart> cart = bookingCartService.findBookingCartByCustomerId(customer.getCustomerId())

        then: "해당 상품은 장바구니에 옵션을 가지지 않는다."
        cart.isPresent()
        cart.get().getProducts().size() == 1
        cart.get().getProducts().get(0).getProductOptions().isEmpty()

        cleanup:
        deleteCustomerAndProducts()
    }
    // TODO
    def "옵션과 함께 상품을 장바구니에 저장 후 조회 시 선택한 옵션이 있어야 한다."()   {
        Long customerId = 1L
        Long productId = 100L
        given: "고객은 옵션 없는 상품을 장바구니에 저장한다."
        bookingCartService.addToCart(customerId, LocalDateTime.now(), productId)
        when: "고객의 장바구니를 조회한다."
        then: "해당 상품은 장바구니에 옵션을 가지지 않는다."
    }

    void createCustomerAndProducts()  {
        this.customer = customerControlService.addCustomer(new Customer(name:"BookingCartServiceIntegration.옵션없는고객", emailAddr:"email@email.com"))
        this.productWithoutOptions = productManager.addNewProduct(
                new Product(productName: "BookingCartServiceSpec.옵션상품", validProduct: true, outOfStock: false))
        this.productWithOptions = productManager.addNewProduct(
                new Product(productName: "BookingCartServiceSpec.옵션상품", validProduct: true, outOfStock: false,
                        productOptions: [new ProductOption(price:1000), new ProductOption(price:2000)]))
    }

    void deleteCustomerAndProducts()    {
        customerControlService.removeCustomerById(customer.getCustomerId())
    }
}
