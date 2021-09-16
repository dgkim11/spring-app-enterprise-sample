package example.spring.hotel.application.product

import example.spring.hotel.application.functionaltest.helper.ProductHelper
import example.spring.hotel.domain.model.product.Product
import example.spring.hotel.domain.model.product.ProductRepository
import example.spring.hotel.web.config.HotelApplicationWebConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDateTime
@Title("상품 리스트에서 보여지는 상품들은 반드시 판매 가능한 상품만을 보여준다")
@Narrative("고객이 쇼핑몰에 최초 접속하거나 로그인하면 볼 수 있는 화면으로 현재 판매중인 상품 목록을 한눈에 보여준다.")
@ContextConfiguration(classes = [HotelApplicationWebConfig.class])
class ShowOnlyValidProductsSpec extends Specification{
    private @Autowired ProductManager productManager
    private @Autowired ProductRepository productFinder
    private @Autowired ProductHelper productHelper

    def "판매 종료날짜가 지난 상품은 보여주지 않는다."()   {
        String productName = "ShowOnlyValidProductsSpec.판매종료상품"
        given: "판매 종료 날짜가 이틀 지난 상품이다."
        productManager.addNewProduct(new Product(
                productName: productName, endSaleDateTime: LocalDateTime.now().minusDays(2), validProduct: true))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "상품을 보여주지 않는다."
        products.size() == 0

        cleanup:
        productHelper.deleteProductByName(productName)

    }
    def "판매 종료날짜가 없는 경우는 무기한 판매 상품이기에 보여준다"()   {
        String productName = "ShowOnlyValidProductsSpec.무기한판매상품"

        given: "판매 종료 날짜가 없는 무기한 판매 상품 등록"
        productManager.addNewProduct(
                new Product(productName: productName, validProduct: true))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "무기한 상품 목록을 보여준다"
        products.size() == 1

        cleanup:
        productHelper.deleteProductByName(productName)
    }

    def "판매 시작날짜가 없는 경우는 즉시 판매 상품이기에 보여준다"()   {
        String productName = "ShowOnlyValidProductsSpec.판매날짜없는상품"

        given: "판매날짜가 없는 상품 등록"
        productManager.addNewProduct(
                new Product(productName: productName, endSaleDateTime: LocalDateTime.now().plusDays(10), validProduct: true))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "무기한 상품 목록을 보여준다"
        products.size() == 1

        cleanup:
        productHelper.deleteProductByName(productName)
    }

    def "invalid로 마크된 상품은 보여주지 않는다."()  {
        String productName = "ShowOnlyValidProductsSpec.invalid상품"

        given: "invalid한 상품으로 변경"
        Product product = productManager.addNewProduct(new Product(productName: productName, validProduct: true))
        productManager.invalidateProduct(product.getProductId())

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "상품을 보여주지 않는다."
        products.size() == 0

        cleanup:
        productHelper.deleteProductByName(productName)
    }

    def "오늘날짜가 판매날짜 기간이 아니면 상품을 보여주지 않는다."()    {
        String productName = "ShowOnlyValidProductsSpec.판매기간이아닌상품"

        given: "판매 기간이 아닌 상품이다"
        productManager.addNewProduct(
                new Product(productName: productName, startSaleDateTime: LocalDateTime.now().plusDays(10),
                        endSaleDateTime: LocalDateTime.now().plusDays(20)))

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "상품을 보여주지 않는다."
        products.size() == 0

        cleanup:
        productHelper.deleteProductByName(productName)
    }

    def "품절된 상품은 보여서는 안된다"()    {
        String productName = "ShowOnlyValidProductsSpec.품절상품"

        given: "상품이 품절되었다"
        Product product = productManager.addNewProduct(
                new Product(productName: productName))
         productManager.invalidateProduct(product.getProductId())

        when: "가용한 상품 목록 가져온다"
        List<Product> products = productFinder.findAllValidProductsWithoutOptions()

        then: "상품을 보여주지 않는다."
        products.size() == 0

        cleanup:
        productHelper.deleteProductByName(productName)
    }
}
