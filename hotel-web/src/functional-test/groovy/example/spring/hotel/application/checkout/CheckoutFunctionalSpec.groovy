package example.spring.hotel.application.checkout

import spock.lang.Specification

class CheckoutFunctionalSpec extends Specification  {
    def "checkout 후 장바구니에 있는 모든 상품이 checkout 목록에 있어야 한다."()    {
        given: "장바구니에 상품을 넣는다."
        when: "checkout을 수행한다."
        then: "장바구니에 있는 상품목록과 checkout에 있는 상품목록이 같다."
    }
    def "결제가 완료되면 장바구니에서 해당 상품들은 사라진다."()   {
        given: "고객은 장바구니에 상품을 checkout 한다."
        when: "고객은 성공적으로 결제를 수행한다."
        then: "해당 고객의 장바구니에 주문한 상품은 사라진다."
    }
    def "결제가 실패하면 장바구니에 해당 상품들은 남아있어야 한다."()    {
        given: "고객은 장바구니에 상품을 checkout 한다."
        when: "고객은 결제가 실패한다."
        then: "해당 고객의 장바구니에 주문 실패한 상품들이 존재한다."
    }
}
