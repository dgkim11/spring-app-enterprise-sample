package example.spring.hotel.infrastructure.domain.mybatis.checkout

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.checkout.Checkout
import example.spring.hotel.domain.model.checkout.CheckoutItem
import example.spring.hotel.domain.model.checkout.CheckoutRepository
import example.spring.hotel.domain.model.checkout.PurchaseHistoryService
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.infrastructure.domain.config.HotelDomainInfraConfig
import example.spring.hotel.infrastructure.domain.config.IntegrationTestConfig
import example.spring.hotel.infrastructure.domain.helper.ProductHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelDomainInfraConfig.class, IntegrationTestConfig.class ])
class CheckoutIntegrationTest extends Specification  {
    @Autowired private CheckoutRepository checkoutRepository
    @Autowired private CheckoutMapper checkoutMapper
    @Autowired private ProductRepository productRepository
    @Autowired private ProductHelper productHelper
    private List<Product> products
    private Checkout checkout
    private PurchaseHistoryService purchaseHistoryService

    def setup() {
        purchaseHistoryService = Stub(PurchaseHistoryService)
        purchaseHistoryService.isPurchasedAlready(_) >> false
        products = createProducts()
    }

    def cleanup()   {
        if(checkout != null)
            checkoutRepository.deleteById(checkout.getCheckoutId())
        deleteProducts()
    }

    def "checkout된 항목이 올바르게 저장되고 조회된다"()    {
        given: "장바구니에 상품들 checkout"
        checkout = createAndSaveCheckout()

        when: "checkout 조회"
        Optional<Checkout> checkoutSaved = checkoutRepository.findById(checkout.getCheckoutId())

        then: "총 금액이 일치하여야 한다."
        checkoutSaved.isPresent()
        checkoutSaved.get().getCustomerId() == checkout.getCustomerId()
        checkoutSaved.get().checkoutItems.size() == checkout.getCheckoutItems().size()
        checkoutSaved.get().getTotalPrice() == checkout.getTotalPrice()
    }

    def "checkout 항목을 삭제한다."()  {
        given: "장바구니에 상품들 checkout"
        checkout = createAndSaveCheckout()

        when: "checkout 삭제"
        checkoutRepository.deleteById(checkout.getCheckoutId())
        int optionCount = checkout.getCheckoutItems().stream().mapToInt(i -> getOptionCount(i)).sum()

        then: "데이타가 없다."
        checkoutMapper.findCheckoutById(checkout.getCheckoutId()) == null
        checkoutMapper.findCheckoutItemsByCheckoutId(checkout.getCheckoutId()).size() == 0
        optionCount ==  0
    }

    private int getOptionCount(CheckoutItem checkoutItem)   {
        return checkoutMapper.findCheckoutProductOptions(checkoutItem.getCheckoutItemId()).size()
    }

    private Checkout createAndSaveCheckout()    {
        Checkout checkout = Checkout.builder().customerId(1L).checkoutDateTime(LocalDateTime.now()).build()
        List<CheckoutItem> checkoutItemList = createCheckoutItemsWithOptions()
        for(CheckoutItem item : checkoutItemList)   {
            checkout.addCheckoutItem(item, purchaseHistoryService)
        }
        checkoutRepository.insert(checkout)

        return checkout
    }
    private List<CheckoutItem> createCheckoutItemsWithOptions() {
        ObjectGraphBuilder builder = new ObjectGraphBuilder()
        builder.classNameResolver = "example.spring.hotel.domain.model.checkout"

        // total price for item1 -> 1100원
        CheckoutItem item1 = builder.checkoutItem(product: products.get(0), bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 100, productOptionId: 100L)
        }
        // total price for item1 -> 2200원
        CheckoutItem item2 = builder.checkoutItem(product: products.get(1), bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 200, productOptionId: 101L)
        }
        // total price for item1 -> 3400원
        CheckoutItem item3 = builder.checkoutItem(product: products.get(2), bookingDateTime: LocalDateTime.now()) {
            checkoutProductOption(optionPrice: 200, productOptionId: 102L)
            checkoutProductOption(optionPrice: 200, productOptionId: 103L)
        }

        return [item1, item2, item3]
    }

    private List<Product> createProducts()    {
        List<Product> products = new ArrayList<>()

        products.add(productHelper.createProductWithoutOption("CheckoutIntegrationTest.product1", 1000L))
        products.add(productHelper.createProductWithoutOption("CheckoutIntegrationTest.product2", 2000L))
        products.add(productHelper.createProductWithoutOption("CheckoutIntegrationTest.product3", 3000L))

        return products
    }

    private void deleteProducts()    {
        products.forEach(product -> {
            System.out.println("product:" + product.getProductName())
            productHelper.deleteProductByName(product.getProductName())
        })
    }
}
