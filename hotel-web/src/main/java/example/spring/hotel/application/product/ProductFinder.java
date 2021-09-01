package example.spring.hotel.application.product;

import example.spring.hotel.domain.product.Product;
import example.spring.hotel.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 상품을 조회하는 기능을 제공한다.
 */
@Service
public class ProductFinder {
    private ProductRepository productRepository;

    public ProductFinder(ProductRepository productRepository)   {
        this.productRepository = productRepository;
    }

    /**
     * 판매 가능한 상품만을 보여준다.
     * @return
     */
    List<Product> findAllValidProducts()    {
        return productRepository.findAllValidProducts();
    }

    Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}
