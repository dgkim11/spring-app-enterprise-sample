package example.spring.hotel.domain.infrastructure.mybatis.product;

import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select * from product where productId = #{productId}")
    Product findById(Long productId);

    @Insert(
            """
            insert into product (
                productName, price, startSaleDateTime, endSaleDateTime, validProduct,contents,createdAt)
            values (
                #{productName}, #{price}, #{startSaleDateTime}, #{endSaleDateTime},
                #{validProduct}, #{contents}, #{createdAt} )
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void save(Product product);

    @Delete("delete from product where productId = #{productId}")
    int deleteById(Long productId);
    @Insert(
            """
            insert into productOption (
                productId, optionName, description, price)
            values (
                #{productId}, #{optionName}, #{description}, #{price} )        
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "optionId")
    int saveProductOption(ProductOption productOption);

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
            and outOfStock = false
        """
    )
    List<Product> findAllValidProductsWithoutOptions();

    // NOTE. production 코드에서는 있으면 안되는 method. OOM 발생. Iterable로 구현해야 함.
    // 여기는 sample 이니까 만든 것임.
    @Select("select * from product")
    List<Product> findAll();

    // NOTE. functional 및 integration test를 위해서 만든 코드로서 product에서 사용하면 안됨.
    @Delete("delete from product where productName = #{productName}")
    int deleteProductByName(String productName);

    @Delete("delete from productOption where productId = #{productId}")
    int deleteProductOptionsByProductId(Long productId);

    // NOTE. functional 및 integration test를 위해서 만든 코드로서 product에서 사용하면 안됨.
    @Delete("delete from productOption where productId in (select productId from product where productName = #{productName})")
    int deleteProductOptionsByProductName(String productName);
}
