package example.spring.hotel.application.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CheckoutRequestItem {
    private Long productId;
    private List<Long> optionIds;
    // YYYYMMDD
    private String bookingDate;
}
