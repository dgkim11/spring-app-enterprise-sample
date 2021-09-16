package example.spring.hotel.domain.model;

import java.util.Optional;

/**
 * 모든 리파지토리는 BaseRepository를 implement 하여야 한다.
 *
 * @param <T> Entity Class
 * @param <ID> Entity ID(Primary key) Type
 */
public interface BaseRepository<T, ID> {
    Optional<T> findById(ID id);
    int deleteById(ID id);
    void insert(T entity);
}
