package example.spring.hotel.application.checkout;

import example.spring.hotel.application.product.ProductManager;
import example.spring.hotel.domain.model.checkout.*;
import example.spring.hotel.domain.model.checkout.exception.CheckoutException;
import example.spring.hotel.domain.model.product.Product;
import example.spring.hotel.domain.model.product.ProductOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private PurchaseHistoryService purchaseHistoryService;
    private CheckoutRepository checkoutRepository;
    private ProductManager productManager;
    private DateTimeFormatter dateTimeFormatter;

    public CheckoutService(PurchaseHistoryService purchaseHistoryService, CheckoutRepository checkoutRepository, ProductManager productManager)   {
        this.purchaseHistoryService = purchaseHistoryService;
        this.checkoutRepository = checkoutRepository;
        this.productManager = productManager;
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    }

    private Checkout createCheckout(Long customerId, List<CheckoutItem> checkoutItems) throws CheckoutException {
        Checkout checkout = new Checkout(customerId);
        for(CheckoutItem checkoutItem: checkoutItems)   {
            checkout.addCheckoutItem(checkoutItem, purchaseHistoryService);
        }
        checkoutRepository.insert(checkout);
        return checkout;
    }

    @Transactional
    public Checkout checkout(Long customerId, List<CheckoutRequestItem> requestItems) throws CheckoutException  {
        List<CheckoutItem> checkoutItems = generateCheckItemList(requestItems);
        return this.createCheckout(customerId, checkoutItems);
    }

    private List<CheckoutItem> generateCheckItemList(List<CheckoutRequestItem> requestItems)   {
        return requestItems.stream().map(this::convertToCheckoutItem).collect(Collectors.toList());
    }

    private CheckoutItem convertToCheckoutItem(CheckoutRequestItem checkoutRequestItem) {
        Product product = productManager.findById(checkoutRequestItem.getProductId()).get();
        return CheckoutItem.builder()
                .product(product)
                .productPrice(product.getPrice())
                .bookingDateTime(LocalDateTime.parse(checkoutRequestItem.getBookingDate(), dateTimeFormatter))
                .checkoutProductOptions(convertToCheckoutProductOptions(checkoutRequestItem, product))
                .build();
    }

    private List<CheckoutProductOption> convertToCheckoutProductOptions(CheckoutRequestItem checkoutRequestItem, Product product) {
        return product.getProductOptions().stream()
                .filter(productOption -> containsOption(productOption, checkoutRequestItem.getOptionIds()))
                .map(productOption -> new CheckoutProductOption(null, productOption.getOptionId(), productOption.getPrice()))
                .collect(Collectors.toList());
    }

    private boolean containsOption(ProductOption productOption, List<Long> optionIds) {
        return optionIds.stream().anyMatch(option -> option == productOption.getOptionId());
    }

    public Checkout findById(Long checkoutId)   {
        Optional<Checkout> checkoutOptional = checkoutRepository.findById(checkoutId);
        if(checkoutOptional.isEmpty()) throw new RuntimeException("존재하지 않는 id. checkoutId:" + checkoutId);
        return checkoutOptional.get();
    }

}
