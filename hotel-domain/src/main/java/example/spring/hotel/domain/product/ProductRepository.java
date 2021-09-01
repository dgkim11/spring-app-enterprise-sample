package example.spring.hotel.domain.product;

import example.spring.hotel.domain.BaseRepository;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product, Long> {
    List<Product> findByProductName(String productName);
    void saveProductOptions(List<ProductOption> productOptions);
    List<Product> findAllValidProducts();
}
