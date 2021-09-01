package example.spring.hotel.infra.database.mybatis.product;

import example.spring.hotel.domain.product.Product;
import example.spring.hotel.domain.product.ProductOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select * from product where productId = #{productId}")
    Product findById(Long productId);

    @Insert(
            """
            insert into product (
                productName, price, categoryId, startSaleDateTime, endSaleDateTime, validProduct,contents,createdAt)
            values (
                #{productName}, #{price}, #{categoryId}, #{startSaleDateTime}, #{endSaleDateTime},
                #{validProduct}, #{contents}, #{createdAt} )
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void save(Product product);

    @Delete("delete from product where productId = #{productId}")
    void deleteById(Long productId);
    @Insert(
            """
            insert into productOption (
                productId, optionName, description, price, createdAt)
            values (
                #{productId}, #{optionName}, #{description}, #{price}, #{createdAt} )        
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "optionId")
    void saveProductOption(ProductOption productOption);

    @Select("select * from productOption where productId = #{productId}")
    List<ProductOption> findProductOptions(Long productId);

    @Select("select * from product where productName = #{productName}")
    List<Product> findByProductName(String productName);

    @Select(
        """
        select * from product 
        where validProduct = true 
            and (endSaleDateTime is null or endSaleDateTime >= now())
            and (startSaleDateTime is null or startSaleDateTime <= now())
        """
    )
    List<Product> findAllValidProducts();

    // NOTE. production 코드에서는 있으면 안되는 method. OOM 발생. Iterable로 구현해야 함.
    // 여기는 sample 이니까 만든 것임.
    @Select("select * from product")
    List<Product> findAll();
}
