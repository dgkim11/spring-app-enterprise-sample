package example.spring.hotel.web.restapi;

import example.spring.hotel.application.bookingcart.BookingCartService;
import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException;
import example.spring.hotel.web.controller.BookingCartItemRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/bookingcart")
public class BookingCartRestController {
    private BookingCartService bookingCartService;
    private DateTimeFormatter dateTimeFormatter;

    public BookingCartRestController(BookingCartService bookingCartService) {
        this.bookingCartService = bookingCartService;
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    }

    @PostMapping("/add/{customerId}")
    public String addToBookingCart(@PathVariable Long customerId, @RequestBody BookingCartItemRequest itemRequest)    {
        try {
            bookingCartService.addToCart(customerId,
                    itemRequest.getProductId(),
                    itemRequest.getOptionIds(),
                    LocalDateTime.parse(itemRequest.getBookingDate(), dateTimeFormatter));
            return "success";
        } catch (AddToCartException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
