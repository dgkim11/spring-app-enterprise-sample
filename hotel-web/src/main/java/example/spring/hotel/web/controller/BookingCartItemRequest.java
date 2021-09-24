package example.spring.hotel.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookingCartItemRequest {
    private Long productId;
    private List<Long> optionIds;
    // YYYYMMDD
    private String bookingDate;
}
