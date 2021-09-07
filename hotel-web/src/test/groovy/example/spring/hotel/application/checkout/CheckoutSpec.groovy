package example.spring.hotel.application.checkout

import example.spring.hotel.application.purchase.PurchaseService
import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutItem
import example.spring.hotel.domain.model.checkout.CheckoutRepository
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDateTime

@Title("Checkout 관련 비즈니스 Specification")
@Narrative("""
사용자는 개별 상품을 즉시 구매하거나 장바구니에 상품을 담은 후에 Checkout하여 장바구니에 있는 모든 상품을 한번에 주문할 수 있다.
""")
class CheckoutSpec extends Specification {
    private Long customerId = 1L
    private CheckoutRepository checkoutRepository
    private PurchaseService purchaseService
    private ProductRepository productRepository
    private CheckoutService checkoutService

    def setup() {
        initCheckoutService()
    }

    void initCheckoutService()  {
        checkoutRepository = Stub(CheckoutRepository)
        purchaseService = Stub(PurchaseService)
        productRepository = Stub(ProductRepository)
        checkoutService = new CheckoutService(checkoutRepository, purchaseService, productRepository)
    }

    def "즉시 구매에서 checkout 시에 구매 상품은 예약 날짜가 지정되어 있어야 한다"() {
        given: "예약날짜 없이 상품 선택"
        CheckoutItem checkoutItem = new CheckoutItem(productId: 1L, bookingDateTime: null)
        CheckoutService checkoutService =
                new CheckoutService(Stub(CheckoutRepository), Stub(PurchaseService), Stub(ProductRepository))

        when: "즉시 구매로 checkout 수행"
        checkoutService.checkout(customerId, checkoutItem)

        then: "checkout 실패"
        thrown(CheckoutException)
    }
    def "장바구니에서 checkout 시에 해당 상품의 예약날짜에 예약이 가능해야 한다"()    {
        given: "사용자 장바구니에 상품 A가 담겨있다"
        CheckoutItem checkoutItem = new CheckoutItem(productId: 1L, bookingDateTime: LocalDateTime.now())
        and: "상품 A가 해당 날짜에 이미 다른 사용자가 구매하였다."
        purchaseService.isPurchased(_,_) >> true    // 이미 구매된 상품

        when: "장바구니에서 checkout 실행"
        checkoutService.checkout(customerId, checkoutItem)

        then: "checkout 실패"
        thrown(CheckoutException)
    }
    def "checkout 시에 모든 상품이 판매 가능하여야 한다[성공케이스]"()   {
        given: "장바구니에 상품이 존재한다."
        CheckoutItem checkoutItem1 = new CheckoutItem(productId: 1L, bookingDateTime: LocalDateTime.now())
        CheckoutItem checkoutItem2 = new CheckoutItem(productId: 2L, bookingDateTime: LocalDateTime.now())
        and: "상품이 모두 판매 가능하다"
        Product sellableProduct1 = Stub(Product)
        sellableProduct1.isProductSellable() >> true
        Product sellableProduct2 = Stub(Product)
        sellableProduct2.isProductSellable() >> true
        productRepository.findByIdWithoutOptions(1) >> Optional.of(sellableProduct1)
        productRepository.findByIdWithoutOptions(2) >> Optional.of(sellableProduct2)

        when: "판매가능한 상품들을 checkout을 수행한다."
        checkoutService.checkout(customerId, [checkoutItem1, checkoutItem2])

        then: "checkout에 성공한다."
        noExceptionThrown()
    }
    def "checkout 시에 모든 상품이 판매 가능하여야 한다[실패케이스]"()   {
        given: "장바구니에 상품이 존재한다."
        CheckoutItem checkoutItem1 = new CheckoutItem(productId: 1L, bookingDateTime: LocalDateTime.now())
        CheckoutItem checkoutItem2 = new CheckoutItem(productId: 2L, bookingDateTime: LocalDateTime.now())
        and: "판매 불가 상품이 존재한다."
        Product sellableProduct = Stub(Product)
        sellableProduct.isProductSellable() >> true
        Product notSellableProduct = Stub(Product)
        notSellableProduct.isProductSellable() >> false
        productRepository.findByIdWithoutOptions(1) >> Optional.of(sellableProduct)
        productRepository.findByIdWithoutOptions(2) >> Optional.of(notSellableProduct)

        when: "해당 상품들을 checkout을 수행한다."
        checkoutService.checkout(customerId, [checkoutItem1, checkoutItem2])

        then: "checkout에 실패한다."
        thrown(CheckoutException)
    }
    def "총 결제 금액이 checkout 목록에 있는 상품의 합과 같아야 한다"()  {
        given: "장바구니에 상품들이 등록되어 있다"
        List<CheckoutItem> checkoutItemList = createCheckoutItemsWithOptions()

        when: "checkout 실행"
        productRepository.findByIdWithoutOptions(_) >> Optional.of(new Product())
        Checkout checkout = checkoutService.checkout(customerId, checkoutItemList)

        then: "총 금액이 일치하여야 한다."
        checkout.getTotalPrice() == 1100 + 2200 + 3400  // 참조 : createCheckoutItemsWithOptions()
    }

    private List<CheckoutItem> createCheckoutItemsWithOptions() {
        ObjectGraphBuilder builder = new ObjectGraphBuilder()
        builder.classNameResolver = "example.spring.hotel.domain.model.checkout"

        // total price for item1 -> 1100원
        CheckoutItem item1 = builder.checkoutItem(productId: 1L, productPrice:1000L, bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 100)
        }
        // total price for item1 -> 2200원
        CheckoutItem item2 = builder.checkoutItem(productId: 1L, productPrice:2000L, bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 200)
        }
        // total price for item1 -> 3400원
        CheckoutItem item3 = builder.checkoutItem(productId: 1L, productPrice:3000L, bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 200)
            checkoutProductOption(optionPrice: 200)
        }

        return [item1, item2, item3]
    }
}
