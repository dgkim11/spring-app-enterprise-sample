package example.spring.hotel.infrastructure.domain.mybatis.purchase

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.payment.PaymentInfo
import example.spring.hotel.domain.model.payment.PaymentType
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository
import example.spring.hotel.domain.model.purchase.PurchasedOrder
import example.spring.hotel.domain.model.purchase.PurchasedProduct
import example.spring.hotel.infrastructure.domain.config.HotelDomainInfraConfig
import example.spring.hotel.infrastructure.domain.config.IntegrationTestConfig
import example.spring.hotel.infrastructure.domain.helper.ProductHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelDomainInfraConfig.class, IntegrationTestConfig.class ])
class PurchaseIntegrationTest extends Specification {
    @Autowired private PurchasedOrderRepository purchasedOrderRepository
    @Autowired private PurchasedOrderMapper mapper
    @Autowired private ProductHelper productHelper
    private PurchasedOrder order
    private Product product1
    private Product product2

    def setup() {
        product1 = productHelper.createProduct("PurchaseIntegrationTest.product1", 1000L, 3)
        product2 = productHelper.createProduct("PurchaseIntegrationTest.product2", 2000L, 3)
    }

    def cleanup()   {
        productHelper.deleteProductByName(product1.getProductName())
        productHelper.deleteProductByName(product2.getProductName())
        mapper.deleteById(order.getPurchasedOrderId())
    }

    def "order를 성공적으로 저장하고 조회한다."()    {
        given:"order를 생성한다."
        order = createOrder()

        when: "order를 저장한다."
        purchasedOrderRepository.insert(order)
        and: "order를 조회한다."
        Optional<PurchasedOrder> orderOptional = purchasedOrderRepository.findById(order.getPurchasedOrderId())

        then: "저장한 데이터와 조회한 데이터가 동일하다."
        orderOptional.isPresent()
        isEqual(order, orderOptional.get())
    }

    private PurchasedOrder createOrder()    {
        return PurchasedOrder.builder()
                        .customerId(1L)
                        .totalPrice(100000L)
                        .purchasedDateTime(LocalDateTime.now())
                        .purchasedProducts([
                                new PurchasedProduct(
                                                    product1.getProductId(),
                                                    [
                                                            product1.getProductOptions().get(0).getOptionId(),
                                                            product1.getProductOptions().get(1).getOptionId()
                                                    ]),
                                new PurchasedProduct(
                                                    product2.getProductId(),
                                                    [
                                                            product2.getProductOptions().get(0).getOptionId(),
                                                            product2.getProductOptions().get(1).getOptionId()
                                                    ])
                        ])
                        .paymentInfoList([
                                new PaymentInfo(PaymentType.CARD, 10000L, "1234"),
                                new PaymentInfo(PaymentType.CARD, 90000L, "5678")
                        ])
                        .build()
    }

    private boolean isEqual(PurchasedOrder order1, PurchasedOrder order2)   {
        return order1.getPurchasedOrderId() == order2.getPurchasedOrderId() &&
                order1.getPurchasedDateTime() == order2.getPurchasedDateTime() &&
                order1.getTotalPrice() == order2.getTotalPrice() &&
                order1.getPurchasedProducts().size() == order2.getPurchasedProducts().size() &&
                order1.getPaymentInfoList().size() == order2.getPaymentInfoList().size()

    }
}
