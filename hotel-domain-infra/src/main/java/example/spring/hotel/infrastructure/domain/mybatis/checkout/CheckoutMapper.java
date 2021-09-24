package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutProductOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckoutMapper {
    @Insert("""
            insert into checkout (customerId, totalPrice, checkoutDateTime)
            values (#{customerId}, #{totalPrice}, #{checkoutDateTime})
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "checkoutId")
    void insertCheckout(Checkout checkout);

    @Insert("""
            insert into checkoutItem (checkoutId, productId, productPrice, bookingDateTime )
            values(#{checkoutId}, #{product.productId}, #{productPrice}, #{bookingDateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "checkoutItemId")
    void insertCheckoutItem(CheckoutItem checkoutItem);

    @Insert("""
            insert into checkoutItemOption (checkoutItemId, productOptionId, optionPrice )
            values (#{checkoutItemId},#{productOptionId}, #{optionPrice})
            """)
    void insertCheckoutProductOption(CheckoutProductOption option);

    @Select("""
            select * from checkout where checkoutId = ${checkoutId}
            """)
    CheckoutRow findCheckoutById(Long checkoutId);

    @Select("""
            select * from checkoutItem where checkoutId = #{checkoutId}
            """)
    List<CheckoutItemRow> findCheckoutItemsByCheckoutId(Long checkoutId);

    @Select("""
            select * from checkoutItemOption where checkoutItemId = #{checkoutItemId}
            """)
    List<CheckoutProductOption> findCheckoutProductOptions(Long checkoutItemId);

    @Delete("""
            delete c, i, o from checkout c inner join checkoutItem i inner join checkoutItemOption o
            on c.checkoutId = i.checkoutId and i.checkoutItemId = o.checkoutItemId
            where c.checkoutId = #{checkoutId}
            """)
    int deleteById(Long checkoutId);

    // NOTE. Integration, 또는 Functional Test용 메서드. production에서 사용하지 말것.
    @Delete("""
            delete c, i, o from checkout c inner join checkoutItem i inner join checkoutItemOption o
            on c.checkoutId = i.checkoutId and i.checkoutItemId = o.checkoutItemId
            where c.customerId = #{customerId}
            """)
    int deleteByCustomerId(Long customerId);
}
