package example.spring.hotel.application.product;

import example.spring.hotel.application.product.exception.InvalidProductException;
import example.spring.hotel.domain.product.Product;
import example.spring.hotel.domain.product.ProductOption;
import example.spring.hotel.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 관리한다.
 */
@Service
public class ProductManager {
    private ProductRepository productRepository;

    public ProductManager(ProductRepository productRepository)  {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product addNewProduct(Product product, List<ProductOption> options) {
        addNewProduct(product);
        for(ProductOption option : options)   {
            product.addProductOption(option);
        }
        productRepository.saveProductOptions(product.getProductOptions());
        return product;
    }

    public Product addNewProduct(Product product)  {
        // 기존에 동일한 상품명이 존재하지 않아야 한다.
        if(! productRepository.findByProductName(product.getProductName()).isEmpty())  {
            throw new InvalidProductException("동일한 상품이 존재합니다. 상품명: " + product.getProductName());
        }
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Long productId)   {
        productRepository.deleteById(productId);
    }

    public void updateProduct(Product product, ProductUpdateRequest updateRequest)  {

    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }
}
