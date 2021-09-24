package example.spring.hotel.infrastructure.domain.mybatis.purchase;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PurchasedOrderMapper {
    @Insert("""
            insert into purchasedOrder (customerId, purchasedOrderSnapshot) values (#{customerId}, #{purchasedOrderSnapshot})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "purchasedOrderId")
    void insert(PurchasedOrderRow row);

    // NOTE. 해당 메서드는 integration 또는 functional test를 위해 만든 것임. production 코드에서는 사용하면 안됨
    @Delete("""
            delete from purchasedOrder where customerId = #{customerId}
            """)
    void deleteByCustomerId(Long customerId);

    @Select("""
            select * from purchasedOrder where purchasedOrderId = #{purchasedOrderId}
            """)
    PurchasedOrderRow findById(Long purchasedOrderId);
}
