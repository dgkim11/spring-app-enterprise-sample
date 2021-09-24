package example.spring.hotel.infrastructure.domain.mybatis.checkout;

import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.CheckoutItem;
import example.spring.hotel.domain.model.checkout.CheckoutProductOption;
import example.spring.hotel.domain.model.checkout.CheckoutRepository;
import example.spring.hotel.domain.model.product.ProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CheckoutRepositoryImpl implements CheckoutRepository {
    private CheckoutMapper mapper;
    private ProductRepository productRepository;
    private Field checkoutItemIdField;
    private Field checkoutIdField;

    public CheckoutRepositoryImpl(CheckoutMapper mapper, ProductRepository productRepository)    {
        this.mapper = mapper;
        this.productRepository = productRepository;
        try {
            checkoutItemIdField = CheckoutProductOption.class.getDeclaredField("checkoutItemId");
            checkoutItemIdField.setAccessible(true);
            checkoutIdField = CheckoutItem.class.getDeclaredField("checkoutId");
            checkoutIdField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
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
        List<CheckoutProductOption> options = mapper.findCheckoutProductOptions(itemRow.getCheckoutItemId());
        return CheckoutItem.builder()
                .checkoutId(itemRow.getCheckoutId())
                .checkoutItemId(itemRow.getCheckoutItemId())
                .bookingDateTime(itemRow.getBookingDateTime())
                .product(productRepository.findById(itemRow.getProductId()).get())
                .productPrice(itemRow.getProductPrice())
                .checkoutProductOptions(options)
                .build();
    }

    @Override
    public int deleteById(Long checkoutId) {
        return mapper.deleteById(checkoutId);
    }

    @Override
    @Transactional
    public void insert(Checkout checkout) {
        mapper.insertCheckout(checkout);
        for(CheckoutItem checkoutItem : checkout.getCheckoutItems())    {
            mapper.insertCheckoutItem(setCheckoutIdField(checkout.getCheckoutId(), checkoutItem));
            insertCheckoutProductOptions(setCheckoutItemId(checkoutItem.getCheckoutItemId(), checkoutItem.getCheckoutProductOptions()));
        }
    }

    private List<CheckoutProductOption> setCheckoutItemId(Long checkoutItemId, List<CheckoutProductOption> checkoutProductOptions) {
        for(CheckoutProductOption option : checkoutProductOptions)  {
            try {
                checkoutItemIdField.set(option, checkoutItemId);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return checkoutProductOptions;
    }

    /**
     * checkoutId는 Checkout 객체가 insert된 후에 생성되는 것이어서 CheckoutItem은 아직 checkoutId를 모른다.
     * 이 상태에서 checkoutId를 넣어주기 위해서는 CheckoutItem이 readOnly이기 때문에 checkoutId 값을 주입해주어야 함.
     */
    private CheckoutItem setCheckoutIdField(Long checkoutId, CheckoutItem checkoutItem) {
        try {
            checkoutIdField.set(checkoutItem, checkoutId);
            return checkoutItem;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertCheckoutProductOptions(List<CheckoutProductOption> checkoutProductOptions) {
        for(CheckoutProductOption option: checkoutProductOptions) {
            mapper.insertCheckoutProductOption(option);
        }
    }
}
