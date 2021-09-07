package example.spring.hotel.domain.infrastructure.mybatis.customer;

import example.spring.hotel.domain.model.customer.Customer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CustomerMapper {
    @Select("select * from customer where customerId = #{customerId}")
    Customer findById(Long customerId);

    @Insert(
            """
            insert into customer (
                customerId, accountId, password, name, emailAddr, createdAt)
            values (
                #{customerId}, #{accountId}, #{password}, #{name}, #{emailAddr}, #{createdAt} )        
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "customerId")
    void save(Customer customer);

    @Delete("delete from customer where customerId = #{customerId}")
    void deleteById(Long customerId);

    // NOTE. 해당 메서드는 functional test를 위해서 만든 메서드이니 production code에서는 사용하지 말것.
    @Delete("delete from customer where name = #{customerName}")
    int deleteByName(String customerName);
}
