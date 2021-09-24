package example.spring.hotel.infrastructure.domain.mybatis.product

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.infrastructure.domain.config.HotelDomainInfraConfig
import example.spring.hotel.infrastructure.domain.config.IntegrationTestConfig
import example.spring.hotel.infrastructure.domain.helper.ProductHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelDomainInfraConfig.class, IntegrationTestConfig.class ])
class ProductRepositoryIntegrationTest extends Specification  {
    @Autowired ProductRepository productRepository
    @Autowired ProductHelper productHelper
    private Product product

    def cleanup()   {
        if(product != null) productHelper.deleteProductByName(product.getProductName())
    }

    def "기본 상품 정보를 입력하고 db에 insert한다."() {
        String productName = "ProductRepositoryIntegrationTest.baseProduct"

        given: "testProduct이란 상품을 DB에 insert 한다"
        product = productHelper.createProductWithoutOption(productName, 1000L)
        productRepository.insert(product)

        when: "해당 상품을 productId로 DB에서 읽어들인다."
        Optional<Product> productOptional = productRepository.findById(product.productId)

        then: "testProduct이란 상품이 존재한다."
        productOptional.isPresent()
        productOptional.get().productName == productName
    }

    def "기본 상품 정보와 상품 옵션 정보를 생성하고 db에 insert한다"()   {
        String productName = "ProductRepositoryIntegrationTest.productWithOption"
        int optionSize = 3

        given: "옵션을 가진 상품을 저장한다."
        product = productHelper.createProduct(productName, 1000L, optionSize)

        when: "해당 상품을 조회한다."
        Optional<Product> productOptional = productRepository.findById(product.productId)

        then: "상품이 존재한다."
        productOptional.isPresent() == true
        productOptional.get().getProductOptions().size() == optionSize
    }

}
