package example.spring.hotel.application.functionaltest.helper;

import example.spring.hotel.infrastructure.domain.mybatis.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}