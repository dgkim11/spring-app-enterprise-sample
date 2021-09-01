package example.spring.hotel.domain;

import example.spring.hotel.domain.product.Product;

import java.util.List;
import java.util.Optional;

/**
 * 모든 리파지토리는 BaseRepository를 implement 하여야 한다.
 *
 * @param <T> Entity Class
 * @param <ID> Entity ID(Primary key) Type
 */
public interface BaseRepository<T, ID> {
    Optional<T> findById(ID id);
    void deleteById(ID id);
    void save(T entity);
    long count();
    void deleteAll();
    List<Product> findAll();    // sample이라서 만든 method.실제 production에서는 이런 메소드가 있으면 안된다. OOM 발생가능. Iterable로 구현해야 함.
}
