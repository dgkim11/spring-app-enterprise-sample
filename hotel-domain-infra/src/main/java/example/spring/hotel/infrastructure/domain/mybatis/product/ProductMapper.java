package example.spring.hotel.infrastructure.domain.mybatis.product;

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
                productName, price, startSaleDateTime, endSaleDateTime, validProduct, outOfStock, contents)
            values (
                #{productName}, #{price}, #{startSaleDateTime}, #{endSaleDateTime},
                #{validProduct}, #{outOfStock}, #{contents}
            )
            """
    )
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void insert(Product product);

    @Delete("delete p, o from product p inner join productOption o on p.productId = o.productId where p.productId = #{productId}")
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
    int insertProductOption(ProductOption productOption);

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

    // NOTE. functional ??? integration test??? ????????? ?????? ???????????? product?????? ???????????? ??????.
    @Delete("delete from product where productName = #{productName}")
    int deleteProductByName(String productName);
    // NOTE. functional ??? integration test??? ????????? ?????? ???????????? product?????? ???????????? ??????.
    @Delete("""
    delete o from productOption o inner join product p 
    where p.productId = o.productId and p.productName = #{productName}
    """)
    int deleteProductOptionsByName(String productName);
}
