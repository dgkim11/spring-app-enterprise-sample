package example.spring.hotel.domain.model.product;

import example.spring.hotel.domain.model.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product, Long> {
    List<Product> findByProductName(String productName);
    int saveProductOptions(List<ProductOption> productOptions);
    int deleteProductOptionsByProductId(Long productId);
    List<Product> findAllValidProductsWithoutOptions();
    Optional<Product> findByIdWithoutOptions(Long productId);
}
