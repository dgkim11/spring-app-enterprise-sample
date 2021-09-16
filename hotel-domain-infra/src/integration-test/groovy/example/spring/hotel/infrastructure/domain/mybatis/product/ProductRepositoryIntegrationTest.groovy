package example.spring.hotel.infrastructure.domain.mybatis.product

import example.spring.hotel.domain.config.HotelDomainConfig
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductOption
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.infrastructure.domain.config.HotelAppDomainInfraConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [HotelDomainConfig.class, HotelAppDomainInfraConfig.class])
class ProductRepositoryIntegrationTest extends Specification  {
    @Autowired ProductRepository productRepository
    @Autowired ProductMapper mapper

    def "기본 상품 정보를 입력하고 db에 insert한다."() {
        String productName = "ProductRepository.baseProduct"

        given: "testProduct이란 상품을 DB에 insert 한다"
        Product product = createProduct(productName)
        productRepository.insert(product)

        when: "해당 상품을 productId로 DB에서 읽어들인다."
        Optional<Product> productOptional = productRepository.findById(product.productId)

        then: "testProduct이란 상품이 존재한다."
        productOptional.isPresent() == true
        productOptional.get().productName == productName

        cleanup:
        mapper.deleteProductByName(productName)
    }

    def "기본 상품 정보와 상품 옵션 정보를 생성하고 db에 insert한다"()   {
        String productName = "ProductRepository.productWithOption"
        int optionSize = 3

        given: "옵션을 가진 상품을 저장한다."
        Product product = createProduct(productName)
        productRepository.insert(product)
        List<ProductOption> options = createProductOptions(product.getProductId(), optionSize)
        productRepository.insertProductOptions(options)

        when: "해당 상품을 조회한다."
        Optional<Product> productOptional = productRepository.findById(product.productId)

        then: "상품이 존재한다."
        productOptional.isPresent() == true
        productOptional.get().getProductOptions().size() == optionSize

        cleanup:
        mapper.deleteProductByName(productName)

    }

//    def "상품이 먼저 등록된 후에 옵션을 추가할 수 있다."() {
//
//    }
//
    Product createProduct(String productName) {
        return Product.builder()
            .productName(productName).price(1000).build();
    }

    List<ProductOption> createProductOptions(Long productId, int size) {
        List<ProductOption> options = new ArrayList<>()
        for(int i = 0;i < size;i++) {
            options.add(new ProductOption(productId: productId, optionName: "option" + i))
        }

        return options
    }
}
