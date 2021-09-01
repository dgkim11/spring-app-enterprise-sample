package example.spring.hotel.application.product

import example.spring.hotel.domain.product.Product
import example.spring.hotel.domain.product.ProductOption
import example.spring.hotel.web.config.HotelApplicationWebConfig
import org.assertj.core.util.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelApplicationWebConfig.class])
class ProductManagerIntegrationTest extends Specification {
    @Autowired private ProductManager productManager

    def "신규 상품의 경우 상품 option 저장에 실패하면 상품도 DB에 저장되지 말아야 한다."()   {
        given: "상품의 옵션정보에서 옵션 이름을 지정하지 않았다."
        Product product = new Product(productName:randomProductName(), price:1000)
        ProductOption option = new ProductOption(optionName: null)    // optionName은 not null이다. 에러 발생

        when: "상품과 상품의 옵션 정보를 저장한다."
        productManager.addNewProduct(product, Lists.list(option))

        then: "저장에 실패하고 상품 데이타도 rollback 된다."
        thrown(Exception)
        productManager.findById(product.getProductId()).isEmpty() == true

        cleanup:
        productManager.deleteProduct(product.getProductId())

    }

    private String randomProductName()  {
        return "product_" + UUID.randomUUID().toString();
    }
}
