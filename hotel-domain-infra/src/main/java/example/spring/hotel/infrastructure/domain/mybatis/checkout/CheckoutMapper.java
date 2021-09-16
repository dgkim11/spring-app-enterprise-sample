package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutProductOption;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckoutMapper {
    @Insert("""
            insert into checkout (customerId, totalPrice, checkoutDateTime)
            values (#{customerId}, #{totalPrice}, #{checkoutDateTime})
            """
    )
    void insertCheckout(Checkout checkout);

    @Insert("""
            insert into checkoutItem (checkoutId, productId, productPrice )
            values(#{checkoutId}, #{productId}, #{productPrice})
            """)
    void insertCheckoutItem(CheckoutItem checkoutItems);

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

    List<CheckoutProductOptionRow> findCheckoutProductOptions(Long checkoutItemId);
}
