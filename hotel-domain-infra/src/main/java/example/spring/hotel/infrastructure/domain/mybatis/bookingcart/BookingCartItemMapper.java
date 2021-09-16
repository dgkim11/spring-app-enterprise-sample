package example.spring.hotel.infrastructure.domain.mybatis.bookingcart;

import example.spring.hotel.domain.model.bookingcart.BookingCartItem;
import example.spring.hotel.domain.model.bookingcart.BookingCartItemOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookingCartItemMapper {
    @Select("select * from bookingCartItem where customerId = #{customerId}")
    List<BookingCartItemRow> findByCustomerId(Long customerId);

    @Select("select * from bookingCartItemOption where cartItemId = #{cartItemId}")
    List<BookingCartItemOption> findItemOptionsByCartItemId(Long cartItemId);

    @Insert("""
            insert into bookingCartItem (
                customerId, productId, bookingDateTime)
            values (
                #{customerId},#{product.productId},#{bookingDateTime}
            )
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "cartItemId")
    int insertBookingCartItem(BookingCartItem bookingCartItem);

    @Insert("""
            insert into bokingCartItemOption (
                cartItemId, productOptionId)
            values (
                #{cartItemId}, #{productOptionId}
            )
            """)
    BookingCartItemOption insertBookingCartProductOption(BookingCartItemOption itemOption);

    // NOTE. 해당 메서드는 functional test를 위해 만든 메서드이니 production 코드에서는 사용하지 말것.
    @Delete("delete from bookingCartItem where customerId in (select customerId from customer where name = #{customerName})")
    int deleteBookingCartItemsByCustomerName(String customerName);

    @Select("select * from bookingCartItem where customerId = #{customerId} and productId = #{productId}")
    List<BookingCartItem> findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    @Delete("delete from bookingCartItem where customerId = #{customerId}")
    int deleteBookingCartItemByCustomerId(Long customerId);

    @Delete("delete from bookingCartItemOption where cartItemId = #{cartItemId}")
    int deleteBookingCartItemOptionByCartItemId(Long cartItemId);
}
