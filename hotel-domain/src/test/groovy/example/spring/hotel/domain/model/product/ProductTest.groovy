package example.spring.hotel.domain.model.product

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class ProductTest extends Specification  {
    @Unroll("validProduct:#valid & outOfStock:#outOfStock & startSaleDateTime:#startSaleDateTime & endSaleDateTime:#endSaleDateTime : 판매가능-#sellable")
    def "판매 가능한 상품 검증"()   {
        given:
        Product product = new Product(productId:1L, validProduct: valid, outOfStock: outOfStock,
                startSaleDateTime: startSaleDateTime, endSaleDateTime: endSaleDateTime)
        when:
        boolean result = product.isProductSellable()
        then:
        result == sellable
        where:
        valid | outOfStock | startSaleDateTime                  | endSaleDateTime                   | sellable
        true  | false       | null                               | null                              | true
        true  | false       | LocalDateTime.now().minusDays(2)   | null                              | true
        true  | false       | null                               | LocalDateTime.now().plusDays(2)   | true
        true  | false       | LocalDateTime.now().minusDays(2)   | LocalDateTime.now().plusDays(2)   | true
        false | false       | null                               | null                              | false
        true  | true        | null                               | null                              | false
        true  | false       | LocalDateTime.now().plusDays(2)    | null                              | false
        true  | false       | null                               | LocalDateTime.now().minusDays(2)  | false
    }
}
