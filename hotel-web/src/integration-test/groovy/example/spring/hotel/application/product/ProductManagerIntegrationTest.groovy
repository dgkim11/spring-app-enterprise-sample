package example.spring.hotel.application.product

import example.spring.hotel.application.integrationtest.helper.ProductHelper
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductOption
import example.spring.hotel.web.config.HotelApplicationWebConfig
import org.assertj.core.util.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelApplicationWebConfig.class])
class ProductManagerIntegrationTest extends Specification {
    @Autowired private ProductManager productManager
    @Autowired private ProductHelper productHelper

    def "신규 상품의 경우 상품 option 저장에 실패하면 상품도 DB에 저장되지 말아야 한다."()   {
        given: "상품의 옵션정보에서 옵션 이름을 지정하지 않았다."
        Product product = new Product(productName:"ProductManagerIntegration.옵션저장실패", price:1000)
        ProductOption option = new ProductOption(optionName: null)    // optionName은 not null이다. 에러 발생

        when: "상품과 상품의 옵션 정보를 저장한다."
        productManager.addNewProduct(product, Lists.list(option))

        then: "저장에 실패하고 상품 데이타도 rollback 된다."
        thrown(Exception)
        productManager.findById(product.getProductId()).isEmpty() == true

        cleanup:
        productHelper.deleteProductByName(product.getProductName())

    }

    def "상품의 옵션 정보를 갖는 상품을 생성한 후 해당 상품을 조회하면 옵션 정보도 가져와야 한다."() {
        given: "옵션 정보를 가지는 상품을 생성한다."
        Product product = productManager.addNewProduct(
                new Product(productName: "ProductManagerIntegration.옵션있는상품", validProduct: true, price: 10000),
                [new ProductOption(optionName: "옵션1", price: 1000L), new ProductOption(optionName: "옵션2", price: 2000L)])

        when: "해당 상품을 조회한다."
        Optional<Product> foundProduct = productManager.findById(product.getProductId())

        then: "상품 옵션 정보를 가진다."
        foundProduct.isPresent()
        foundProduct.get().getProductOptions().get(0).optionName == "옵션1"
        foundProduct.get().getProductOptions().get(0).price == 1000L
        foundProduct.get().getProductOptions().get(1).optionName == "옵션2"
        foundProduct.get().getProductOptions().get(1).price == 2000L

        cleanup:
        productHelper.deleteProductByName(product.getProductName())
    }

    def "상품을 삭제하면 해당 상품의 옵션 항목도 모두 삭제된다."() {
        given: "옵션을 가지는 상품이 있다."
        Product product = productManager.addNewProduct(
                new Product(productName: "ProductManagerIntegration.옵션있는상품", validProduct: true, price: 10000),
                [new ProductOption(optionName: "옵션1", price: 1000L), new ProductOption(optionName: "옵션2", price: 2000L)])
        and: "해당 상품을 제거한다."
        productManager.deleteProduct(product.getProductId())

        when: "해당 상품의 옵션 정보를 조회한다."
        List<ProductOption> options = productHelper.findProductOptions(product.getProductId())

        then: "해당 상품의 옵션 정보도 제거된다."
        options.isEmpty()

        cleanup:
        productHelper.deleteProductByName(product.getProductName())
    }
}
