package example.spring.hotel.application.product

import example.spring.hotel.application.product.exception.InvalidProductException
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import spock.lang.Specification

/**
 * Unit Test의 경우 Spring bean도 일반 객체처럼 생성하여 테스트한다. Spring framework 위에서 unit test를 하지 않는다.
 */
class ProductManagerTest extends Specification {
    def "이미 상품명이 존재하는 경우 같은 이름으로 상품을 등록할 수 없다"()    {
        String productName = "엠베서버호텔 스위트룸"

        given: "'#productName' 상품명을 가진 상품이 있다"
        ProductRepository repository = Stub(ProductRepository)
        repository.findByProductName(_) >> [productName]
        ProductManager productManager = new ProductManager(repository)

        when: "같은 상품명으로 신규 상품을 추가한다."
        productManager.addNewProduct(new Product(productName: productName), null)

        then: "InvalidProductException을 발생시킨다"
        thrown(InvalidProductException)
    }
}
