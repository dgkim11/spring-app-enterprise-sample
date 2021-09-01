package example.spring.hotel.application.product

import example.spring.hotel.domain.product.Product
import example.spring.hotel.web.config.HotelApplicationWebConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = [HotelApplicationWebConfig.class])
class ShowOnlyValidProductsSpec extends Specification{
    private @Autowired ProductManager productManager
    private @Autowired ProductFinder productFinder

    def "판매 종료날짜가 지난 상품은 보여주지 않는다."()   {
        given: "판매 종료 날짜가 이틀 지난 상품이다."
        deleteAllProducts()
        productManager.addNewProduct(new Product(
                productName: "판매종료 상품", endSaleDateTime: LocalDateTime.now().minusDays(2), validProduct: true, categoryId: 1, createdAt: LocalDateTime.now()))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProducts()

        then: "상품을 보여주지 않는다."
        products.size() == 0
    }
    def "판매 종료날짜가 없는 경우는 무기한 판매 상품이기에 보여준다"()   {
        given:
        deleteAllProducts()
        productManager.addNewProduct(
                new Product(productName: "무기한 판매 상품이다", validProduct: true, categoryId: 1, createdAt: LocalDateTime.now()))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProducts()

        then: "무기한 상품 목록을 보여준다"
        products.size() == 1
    }

    def "판매 시작날짜가 없는 경우는 즉시 판매 상품이기에 보여준다"()   {
        given:
        deleteAllProducts()
        productManager.addNewProduct(
                new Product(productName: "즉시 판매 상품이다", endSaleDateTime: LocalDateTime.now().plusDays(10),
                        validProduct: true, categoryId: 1, createdAt: LocalDateTime.now()))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProducts()

        then: "무기한 상품 목록을 보여준다"
        products.size() == 1
    }

    def "invalid로 마크된 상품은 보여주지 않는다."()  {
        given:
        deleteAllProducts()
        productManager.addNewProduct(
                new Product(productName: "invalid로 설정된 상품이다", validProduct: false, categoryId: 1, createdAt: LocalDateTime.now()))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProducts()

        then: "상품을 보여주지 않는다."
        products.size() == 0
    }

    def "오늘날짜가 판매날짜 기간이 아니면 상품을 보여주지 않는다."()    {
        given: "판매 기간이 아닌 상품이다"
        deleteAllProducts()
        productManager.addNewProduct(
                new Product(productName: "판매기간아닌 상품", startSaleDateTime: LocalDateTime.now().plusDays(10),
                        endSaleDateTime: LocalDateTime.now().plusDays(20), categoryId: 1, createdAt: LocalDateTime.now()))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProducts()

        then: "상품을 보여주지 않는다."
        products.size() == 0
    }

    def deleteAllProducts() {
        for(Product product : productFinder.findAll()) {
            productManager.deleteProduct(product.getProductId())
        }
    }
}
