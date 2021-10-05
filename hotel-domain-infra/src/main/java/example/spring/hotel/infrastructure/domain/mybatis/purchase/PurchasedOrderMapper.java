package example.spring.hotel.infrastructure.domain.mybatis.purchase;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PurchasedOrderMapper {
    @Insert("""
            insert into purchasedOrder (purchasedOrderSnapshot) values (#{purchasedOrderSnapshot})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "purchasedOrderId")
    void insert(PurchasedOrderRow row);

    @Select("""
            select * from purchasedOrder where purchasedOrderId = #{purchasedOrderId}
            """)
    PurchasedOrderRow findById(Long purchasedOrderId);

    // NOTE. 해당 메서드는 Test용으로만 사용한다. production에서는 사용하지 않음.
    @Delete("""
            delete from purchasedOrder where purchasedOrderId = #{purchasedOrderId}
            """)
    int deleteById(Long purchasedOrderId);
}
