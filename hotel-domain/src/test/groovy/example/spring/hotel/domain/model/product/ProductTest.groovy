package example.spring.hotel.domain.model.product

import spock.lang.Specification

import java.time.LocalDateTime

class ProductTest extends Specification {
    def "판매종료 날짜를 지난 상품은 판매 불가능이다."()    {
        given: "판매 종료 시간이 지난 상품"
        Product product = Product.builder()
                                .validProduct(true)
                                .outOfStock(false)
                                .endSaleDateTime(LocalDateTime.now().minusDays(1)).build()

        when: "판매 불가능 여부 확인"
        boolean sellable = product.isProductSellable()

        then: "판매 불가"
        ! sellable
    }
    def "지금 시간이 판매 시작 시간보다 이르면 판매 불가능이다"()  {
        given: "판매 시작 시간이 되지 않은 상품"
        Product product = Product.builder()
                .validProduct(true)
                .outOfStock(false)
                .startSaleDateTime(LocalDateTime.now().plusDays(1)).build()
        when: "판매 불가능 여부 확인"
        boolean sellable = product.isProductSellable()

        then: "판매 불가"
        ! sellable
    }
    def "invalid 마크된 상품은 판매 불가능이다."()   {
        given: "invalid 상품"
        Product product = Product.builder()
                .validProduct(false)
                .outOfStock(false)
                .build()

        when: "판매 불가능 여부 확인"
        boolean sellable = product.isProductSellable()

        then: "판매 불가"
        ! sellable

    }
    def "품절 상품은 판매 불가능이다."()    {
        given: "품절상품"
        Product product = Product.builder()
                .validProduct(true)
                .outOfStock(true)
                .build()

        when: "판매 불가능 여부 확인"
        boolean sellable = product.isProductSellable()

        then: "판매 불가"
        ! sellable

    }
}
