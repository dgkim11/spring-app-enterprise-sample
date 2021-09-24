package example.spring.hotel.infrastructure.domain.mybatis.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.spring.hotel.domain.model.purchase.PurchasedOrderRepository;
import example.spring.hotel.domain.model.purchase.PurchasedOrder;
import example.spring.hotel.domain.model.purchase.PurchasedProduct;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PurchasedOrderRepositoryImpl implements PurchasedOrderRepository {
    private PurchasedOrderMapper purchasedOrderMapper;
    private Field purchasedOrderFiled;
    private ObjectMapper objectMapper;

    public PurchasedOrderRepositoryImpl(PurchasedOrderMapper orderMapper, ObjectMapper objectMapper)   {
        this.purchasedOrderMapper = orderMapper;
        this.objectMapper = objectMapper;
        try {
            purchasedOrderFiled = PurchasedOrder.class.getDeclaredField("purchasedOrderId");
            purchasedOrderFiled.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PurchasedOrder> findById(Long purchasedOrderId) {
        PurchasedOrderRow row = purchasedOrderMapper.findById(purchasedOrderId);
        if(row == null) return Optional.empty();

        try {
            PurchasedOrder order = objectMapper.readValue(row.getPurchasedOrderSnapshot(), PurchasedOrder.class);
            setPurchasedOrderId(order, row.getPurchasedOrderId());
            return Optional.of(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 사용 안함. order는 삭제되서는 안됨.
     * @param aLong
     * @return
     */
    @Override
    public int deleteById(Long aLong) {
        throw new RuntimeException("지원되지 않음.");
    }

    @Override
    public void insert(PurchasedOrder order) {
        PurchasedOrderRow row = new PurchasedOrderRow(order.getCustomerId(), toJson(order));
        purchasedOrderMapper.insert(row);
        setPurchasedOrderId(order, row.getPurchasedOrderId());
    }

    private String toJson(PurchasedOrder order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPurchasedOrderId(PurchasedOrder order, Long purchasedOrderId) {
        try {
            purchasedOrderFiled.set(order, purchasedOrderId);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PurchasedProduct> findByProductIdAndBookingDateTime(Long productId, LocalDateTime bookingDateTime) {
        return Optional.empty();
    }
}
