package example.spring.hotel.infra.database.mybatis.product

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = HotelDomainConfig.class)
class ProductRepositoryIntegrationTest extends Specification  {
    @Autowired ProductRepository productRepository

    def "기본 상품 정보를 입력하고 db에 insert한다."() {
        given: "testProduct이란 상품을 DB에 insert 한다"
        Product product = createProduct("testProduct")
        productRepository.save(product)

        when: "해당 상품을 productId로 DB에서 읽어들인다."
        Optional<Product> productOptional = productRepository.findById(product.productId)

        then: "testProduct이란 상품이 존재한다."
        productOptional.isPresent() == true
        productOptional.get().productName == "testProduct"

        cleanup:
        productRepository.deleteById(product.productId)
    }

//    def "기본 상품 정보와 상품 옵션 정보를 생성하고 db에 insert한다"()   {
//        given:
//        ProductEntity product = createProductWithOption("optionProduct", 3)
//        productRepository.save(product)
//
//        when:
//        Optional<ProductEntity> productOptional = productRepository.findById(product.productId)
//
//        then:
//        productOptional.isPresent() == true
//        productOptional.get().getProductOptions().size() == 3
//    }
//
//    def "상품이 먼저 등록된 후에 옵션을 추가할 수 있다."() {
//
//    }
//
    Product createProduct(String productName) {
        return Product.builder()
            .productName(productName).categoryId(1).price(1000).createdAt(LocalDateTime.now()).build();
    }
//
//    ProductEntity createProductWithOption(String productName, int size) {
//        ProductEntity product = new ProductEntity(productName:productName, categoryId:1, price:1000, createdAt:LocalDateTime.now())
//        for(int i = 0;i < size;i++) {
//            product.addProductOption("option" + i, 100, "옵션이다" + i)
//        }
//
//        return product
//    }
}
