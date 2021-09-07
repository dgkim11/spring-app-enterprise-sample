package example.spring.hotel.domain.infrastructure.mybatis.product;

import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductOption;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class ProductRepositoryImpl implements ProductRepository {
    private ProductMapper productMapper;

    public ProductRepositoryImpl(ProductMapper productMapper)    {
        this.productMapper = productMapper;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        Optional<Product> productOptional = findByIdWithoutOptions(productId);
        if(productOptional.isEmpty()) return productOptional;

        Product product = productOptional.get();
        for(ProductOption option: productMapper.findProductOptions(product.getProductId()))    {
            product.addProductOption(option);
        }
        return productOptional;
    }

    @Override
    public Optional<Product> findByIdWithoutOptions(Long productId) {
        return Optional.ofNullable(productMapper.findById(productId));
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return productMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void save(Product entity) {
        productMapper.save(entity);
    }

    public int saveProductOptions(List<ProductOption> productOptions)    {
        int rows = 0;
        for(ProductOption productOption : productOptions)   {
            rows += productMapper.saveProductOption(productOption);
        }
        return rows;
    }

    @Override
    public int deleteProductOptionsByProductId(Long productId) {
        return productMapper.deleteProductOptionsByProductId(productId);
    }

    @Override
    public List<Product> findAllValidProductsWithoutOptions() {
        return productMapper.findAllValidProductsWithoutOptions();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    @Override
    public List<Product> findByProductName(String productName) {
        return productMapper.findByProductName(productName);
    }
}
