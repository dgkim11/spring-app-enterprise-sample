package example.spring.hotel.infrastructure.domain.mybatis.bookingcart;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Mybatis를 위한 table mapping용 객체.
 */
@Builder
@Getter
public class BookingCartItemRow {
    private Long cartItemId;
    private Long customerId;
    private Long productId;
    LocalDateTime bookingDateTime;
}
