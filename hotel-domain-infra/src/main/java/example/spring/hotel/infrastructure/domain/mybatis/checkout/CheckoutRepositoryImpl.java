package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutProductOption;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CheckoutRepositoryImpl implements CheckoutRepository {
    private CheckoutMapper mapper;
    private ProductRepository productRepository;

    public CheckoutRepositoryImpl(CheckoutMapper mapper, ProductRepository productRepository)    {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Checkout> findById(Long checkoutId) {
        // === Checkout 엔티티는 하부에 CheckoutItem, CheckoutProductOption 등등의 테이블과 연계되어 있는 root entity이다.
        // === 또한, Checkout은 외부에서는 read only이기 때문에 객체 생성시 모든 필드를 한번에 설정해주어야 한다.
        // === 따라서, Checkout 객체를 만드려면 CheckoutItem이 먼저 만들어져 있어야 한다.
        CheckoutRow checkoutRow = mapper.findCheckoutById(checkoutId);
        if(checkoutRow == null) return Optional.empty();

        List<CheckoutItemRow> checkoutItemRows = mapper.findCheckoutItemsByCheckoutId(checkoutId);
        List<CheckoutItem> checkoutItems = new ArrayList<>();
        for(CheckoutItemRow item: checkoutItemRows) {
            checkoutItems.add(generateCheckoutItem(item));
        }
        Checkout checkout = Checkout.builder()
                .checkoutId(checkoutRow.getCheckoutId())
                .customerId(checkoutRow.getCustomerId())
                .totalPrice(checkoutRow.getTotalPrice())
                .checkoutItems(checkoutItems)
                .checkoutDateTime(checkoutRow.getCheckoutDateTime())
                .build();

        return Optional.of(checkout);
    }

    private CheckoutItem generateCheckoutItem(CheckoutItemRow itemRow) {
        List<CheckoutProductOptionRow> optionRows = mapper.findCheckoutProductOptions(itemRow.getCheckoutItemId());
        return CheckoutItem.builder()
                .checkoutItemId(itemRow.getCheckoutItemId())
                .bookingDateTime(itemRow.getBookingDateTime())
                .product(productRepository.findById(itemRow.getProductId()).get())
                .productPrice(itemRow.getProductPrice())
                .checkoutProductOptions(toCheckoutProductOptions(optionRows))
                .build();
    }

    private List<CheckoutProductOption> toCheckoutProductOptions(List<CheckoutProductOptionRow> optionRows) {
        List<CheckoutProductOption> options = new ArrayList<>();
        for(CheckoutProductOptionRow optionRow : optionRows)    {
            options.add(new CheckoutProductOption(optionRow.getProductOptionId(), optionRow.getOptionPrice()));
        }
        return options;
    }

    @Override
    @Transactional
    public int deleteById(Long aLong) {
        return 0;
    }

    @Override
    @Transactional
    public void insert(Checkout checkoutRow) {
        mapper.insertCheckout(checkoutRow);
        for(CheckoutItem checkoutItem : checkoutRow.getCheckoutItems())    {
            mapper.insertCheckoutItem(checkoutItem);
            insertCheckoutProductOptions(checkoutItem.getCheckoutProductOptions());
        }
    }

    private void insertCheckoutProductOptions(List<CheckoutProductOption> checkoutProductOptions) {
        for(CheckoutProductOption option: checkoutProductOptions) {
            mapper.insertCheckoutProductOption(option);
        }
    }
}
