package example.spring.hotel.application.product;

import example.spring.hotel.application.product.exception.InvalidProductException;
import example.spring.hotel.application.product.exception.ProductNotFoundException;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductOption;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 관리한다. ProductManager는 application service로서 business use case를 구현하는데 필요한 로직들을 가지고 있다.
 * 또한, presentation layer인 controller에서 직접 repository나 엔티티를 건들지 않고 애플리케이션의 behavior만을 생각할 수 있도록
 * setXXX과 같은 메서드가 아닌 명확한 business 행위를 메서드로 만든다.
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

    @Transactional
    public Product addNewProduct(Product product)  {
        // 기존에 동일한 상품명이 존재하지 않아야 한다.
        if(! productRepository.findByProductName(product.getProductName()).isEmpty())  {
            throw new InvalidProductException("동일한 상품이 존재합니다. 상품명: " + product.getProductName());
        }
        productRepository.save(product);
        return product;
    }

    @Transactional
    public void invalidateProduct(Long productId)   {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) throw new ProductNotFoundException("productId: " + productId);
        Product product = productOptional.get();
        product.invalateProduct();
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId)   {
        productRepository.deleteById(productId);
        productRepository.deleteProductOptionsByProductId(productId);

    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }
}
