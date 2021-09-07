package example.spring.hotel.application.product;

import example.spring.hotel.domain.model.product.ProductOption;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductUpdateRequest {
    private String productName;
    private long price;
    private Long categoryId;
    private LocalDateTime startSaleDateTime;
    private LocalDateTime endSaleDateTime;
    private boolean validProduct;
    private List<ProductOption> productOptions = new ArrayList<>();
    private String contents;
}
