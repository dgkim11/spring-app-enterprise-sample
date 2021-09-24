package example.spring.hotel.application.checkout

import com.fasterxml.jackson.databind.ObjectMapper
import example.spring.hotel.application.functionaltest.helper.ProductHelper
import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.customer.Customer
import example.spring.hotel.domain.model.payment.PaymentInfo
import example.spring.hotel.domain.model.payment.PaymentType
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductOption
import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository
import example.spring.hotel.infrastructure.domain.mybatis.bookingcart.BookingCartItemMapper
import example.spring.hotel.infrastructure.domain.mybatis.checkout.CheckoutMapper
import example.spring.hotel.infrastructure.domain.mybatis.customer.CustomerMapper
import example.spring.hotel.infrastructure.domain.mybatis.payment.PaymentMapper
import example.spring.hotel.infrastructure.domain.mybatis.purchase.PurchasedOrderMapper
import example.spring.hotel.infrastructure.domain.payment.PaymentGatewayAdapterForKCP
import example.spring.hotel.web.controller.BookingCartItemRequest
import example.spring.hotel.web.controller.PaymentRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.util.stream.Collectors

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutFunctionalTest extends Specification  {
    @Autowired private ProductHelper productHelper
    @Autowired CheckoutMapper checkoutMapper
    @Autowired BookingCartItemMapper bookingCartItemMapper
    @Autowired private MockMvc mockMvc
    @Autowired CustomerMapper customerMapper
    @Autowired PaymentGatewayAdapterForKCP kcpPg
    @Autowired PaymentMapper paymentMapper
    @Autowired PurchasedOrderMapper orderMapper

    private ObjectMapper objectMapper = new ObjectMapper()

    private Customer customer
    private Product product

    def setup() {
        product = productHelper.createProduct("CheckoutFunctionalSpec.product", 10000L, 3)
        customer = new Customer(null, "CheckoutFunctionalTest.customer", "userid", "password", "email@email.com")
        customerMapper.insert(customer)
    }

    def cleanup()   {
        productHelper.deleteProductByName(product.getProductName())
        bookingCartItemMapper.deleteBookingCartItemsByCustomerName(customer.getName())
        bookingCartItemMapper.deleteBookingCartItemOptionsByCustomerId(customer.getCustomerId())
        checkoutMapper.deleteByCustomerId(customer.getCustomerId())
        customerMapper.deleteByName(customer.getName())
        paymentMapper.deletePaymentByCustomerId(customer.getCustomerId())
    }

    def "checkout 후 장바구니에 있는 모든 상품이 checkout 목록에 있어야 한다."()    {
        given: "장바구니에 상품을 넣는다."
        def requestCart = MockMvcRequestBuilders.post("/bookingcart/add/${customer.getCustomerId()}")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(createBookingCartItemRequest()))
        mockMvc.perform(requestCart).andExpect(status().isOk())

        when: "checkout을 수행한다."
        def requestCheckout = MockMvcRequestBuilders.post("/checkout/${customer.getCustomerId()}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createCheckoutRequestItems()))

        then: "장바구니에 있는 상품목록과 checkout에 있는 상품목록이 같다."
        mockMvc.perform(requestCheckout)
                .andExpect(status().isOk())
                .andExpect(model().attribute("checkout", Matchers.is(new CheckoutMatcher(product, customer))))
    }
    def "결제가 완료되면 장바구니에서 해당 상품들은 사라진다."()   {
        given: "고객은 장바구니에 상품을 담는다."
        def requestCart = MockMvcRequestBuilders.post("/bookingcart/add/${customer.getCustomerId()}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createBookingCartItemRequest()))
        mockMvc.perform(requestCart).andExpect(status().isOk())
        and: "장바구니 상품을 checkout 한다."
        def requestCheckout = MockMvcRequestBuilders.post("/checkout/${customer.getCustomerId()}")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createCheckoutRequestItems()))
        Checkout checkout = mockMvc.perform(requestCheckout).andExpect(status().isOk()).andReturn().getModelAndView().getModel().get("checkout")

        when: "고객은 성공적으로 결제를 수행한다."
        def requestPayment = MockMvcRequestBuilders.post("/payment/execute")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createPaymentRequest(checkout)))
        mockMvc.perform(requestPayment).andExpect(status().isOk())

        then: "해당 고객의 장바구니에 주문한 상품은 사라진다."
        def requestCartView =
                MockMvcRequestBuilders
                        .get("/bookingcart/${customer.getCustomerId()}")
                        .contentType("application/json")
                mockMvc.perform(requestCartView)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().getResponse().getContentAsString().contains("장바구니가 비었습니다")

    }

    private BookingCartItemRequest createBookingCartItemRequest()   {
        return new BookingCartItemRequest(
                product.getProductId(),
                toArray(product.getProductOptions()),
                "2021122500")
    }

    private List<CheckoutRequestItem> createCheckoutRequestItems() {
        return Arrays.asList(new CheckoutRequestItem(
                product.getProductId(),
                toArray(product.getProductOptions()),
                "2021122500"))
    }

    private PaymentRequest createPaymentRequest(Checkout checkout)   {
        return new PaymentRequest(
                kcpPg.companyId(),
                checkout.getCheckoutId(),
                [
                        new PaymentInfo(PaymentType.CARD, checkout.getTotalPrice() - 1000, "1234"),
                        new PaymentInfo(PaymentType.BANK, 1000, "5678"),
                ])
    }

    private static class CheckoutMatcher implements Matcher<Checkout>   {
        private Product product
        private Customer customer

        CheckoutMatcher(Product product, Customer customer)  {
            this.product = product
            this.customer = customer
        }

        private static long getTotalPrice(Product product) {
            return product.getPrice() +
                    product.getProductOptions().stream().mapToLong(option -> option.getPrice()).sum()
        }

        @Override
        boolean matches(Object obj) {
            Checkout checkout = (Checkout)obj
            return checkout.getCustomerId() == customer.getCustomerId() &&
                    checkout.getTotalPrice() == getTotalPrice(product) &&
                    checkout.getCheckoutItems().size() == 1 &&
                    checkout.getCheckoutItems().get(0).getProduct().getProductId() == product.getProductId()
        }

        @Override
        void describeMismatch(Object actual, Description mismatchDescription) {

        }

        @Override
        void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

        }

        @Override
        void describeTo(Description description) {

        }
    }


    private static List<Long> toArray(List<ProductOption> productOptions)    {
        return productOptions.stream().map(option -> option.getOptionId()).collect(Collectors.toList())
    }
}
