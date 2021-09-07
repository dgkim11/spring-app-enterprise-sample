package example.spring.hotel.application.integrationtest.helper;

import example.spring.hotel.domain.infrastructure.mybatis.product.ProductMapper;
import example.spring.hotel.domain.model.product.ProductOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Integration test를 위해서만 필요한 작업들을 제공하는 helper 이다.
 * 여러가지 초기화하는 작업이나 반복적인 작업들을 매번 test 코드안에 넣지 말고 별도로 helper 클래스에 구현하여 test case를 간결하게 만들고
 * 가독성을 높인다. 또한, test를 위해서만 존재하는 internal 메서드가 있는 경우 이것도 helper를 통해 호출하도록 함으로써
 * 혹시라도 internal 메서드가 product에서 호출되는 것을 방지할 수 있다.
 */
@Component
public class ProductHelper {
    @Autowired
    private ProductMapper productMapper;

    public ProductHelper(ProductMapper mapper)  {
        this.productMapper = mapper;
    }

    public void deleteProductByName(String productName) {
        productMapper.deleteProductByName(productName);
        productMapper.deleteProductOptionsByProductName(productName);
    }

    public List<ProductOption> findProductOptions(Long productId)   {
        return productMapper.findProductOptions(productId);
    }
}
