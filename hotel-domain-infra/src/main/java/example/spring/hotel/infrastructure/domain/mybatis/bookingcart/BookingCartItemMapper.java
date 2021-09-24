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
            insert into bookingCartItemOption (
                cartItemId, productOptionId)
            values (
                #{cartItemId}, #{productOptionId}
            )
            """)
    int insertBookingCartItemOption(BookingCartItemOption itemOption);

    // NOTE. 해당 메서드는 functional test를 위해 만든 메서드이니 production 코드에서는 사용하지 말것.
    @Delete("delete from bookingCartItem where customerId in (select customerId from customer where name = #{customerName})")
    int deleteBookingCartItemsByCustomerName(String customerName);

    @Delete("""
            delete from bookingCartItem where customerId = #{customerId}                             
            """)
    int deleteBookingCartItemsByCustomerId(Long customerId);

    @Delete("""
            delete from bookingCartItem where cartItemId = #{cartItemId}                             
            """)
    void deleteBookingCartItemByCartItemId(Long cartItemId);

    @Delete("""
            delete from bookingCartItemOption where cartItemId = #{cartItemId}
            """)
    void deleteBookingCartItemOptionsByCartItemId(Long cartItemId);

    @Delete("""
            delete o from bookingCartItemOption o inner join bookingCartItem i on o.cartItemId = i.cartItemId
            where i.customerId = #{customerId}
            """)
    void deleteBookingCartItemOptionsByCustomerId(Long customerId);
}
