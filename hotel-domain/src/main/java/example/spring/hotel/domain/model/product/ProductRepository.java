package example.spring.hotel.domain.model.product;

import example.spring.hotel.domain.model.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product, Long> {
    List<Product> findByProductName(String productName);
    int insertProductOptions(List<ProductOption> productOptions);
    List<Product> findAllValidProductsWithoutOptions();
    Optional<Product> findByIdWithoutOptions(Long productId);
}
