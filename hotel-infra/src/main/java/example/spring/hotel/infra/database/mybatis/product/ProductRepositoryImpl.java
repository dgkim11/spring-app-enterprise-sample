package example.spring.hotel.infra.database.mybatis.product;

import example.spring.hotel.domain.product.Product;
import example.spring.hotel.domain.product.ProductOption;
import example.spring.hotel.domain.product.ProductRepository;
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
        Optional<Product> productOptional = Optional.ofNullable(productMapper.findById(productId));

        if(productOptional.isEmpty()) return productOptional;

        Product product = productOptional.get();
        for(ProductOption option: productMapper.findProductOptions(product.getProductId()))    {
            product.addProductOption(option);
        }
        return productOptional;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void save(Product entity) {
        productMapper.save(entity);
    }

    public void saveProductOptions(List<ProductOption> productOptions)    {
        for(ProductOption productOption : productOptions)   {
            productMapper.saveProductOption(productOption);
        }
    }

    @Override
    public List<Product> findAllValidProducts() {
        return productMapper.findAllValidProducts();
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
