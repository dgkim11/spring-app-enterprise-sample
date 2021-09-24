package example.spring.hotel.application.functionaltest.helper;

import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductOption;
import example.spring.hotel.domain.model.product.ProductRepository;
import example.spring.hotel.infrastructure.domain.mybatis.product.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductHelper {
    private ProductMapper productMapper;
    private ProductRepository productRepository;

    public ProductHelper(ProductMapper productMapper, ProductRepository productRepository)   {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public void deleteProductByName(String productName) {
        productMapper.deleteProductOptionsByName(productName);
        productMapper.deleteProductByName(productName);
    }

    public Product createProduct(String productName, long price, int optionSize) {
        Product product = Product.builder().productName(productName).price(price).build();
        productRepository.insert(product);
        List<ProductOption> options = createProductOptions(product.getProductId(), optionSize);
        productRepository.insertProductOptions(options);
        options.forEach(option -> product.addProductOption(option));

        return product;
    }

    public Product createProductWithoutOption(String productName, long price) {
        Product product = Product.builder().productName(productName).price(price).build();
        productRepository.insert(product);

        return product;
    }

    List<ProductOption> createProductOptions(Long productId, int size) {
        List<ProductOption> options = new ArrayList<>();
        for(int i = 0;i < size;i++) {
            options.add(ProductOption.builder().productId(productId).optionName("option" + i).price(2000).build());
        }

        return options;
    }

}