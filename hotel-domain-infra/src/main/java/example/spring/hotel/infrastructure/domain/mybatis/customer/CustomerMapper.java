package example.spring.hotel.infrastructure.domain.mybatis.customer;

import example.spring.hotel.domain.model.customer.Customer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CustomerMapper {
    @Select("select * from customer where customerId = #{customerId}")
    Customer findById(Long customerId);

    @Insert(
            """
            insert into customer (
                customerId, userId, password, name, emailAddr)
            values (
                #{customerId}, #{userId}, #{password}, #{name}, #{emailAddr} )        
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "customerId")
    void insert(Customer customer);

    @Delete("delete from customer where customerId = #{customerId}")
    int deleteById(Long customerId);

    // NOTE. 해당 메서드는 functional test를 위해서 만든 메서드이니 production code에서는 사용하지 말것.
    @Delete("delete from customer where name = #{customerName}")
    int deleteByName(String customerName);
}
