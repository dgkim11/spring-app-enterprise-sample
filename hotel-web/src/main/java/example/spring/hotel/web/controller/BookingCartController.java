package example.spring.hotel.web.controller;

import example.spring.hotel.application.bookingcart.BookingCartService;
import example.spring.hotel.domain.model.bookingcart.BookingCart;
import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/bookingcart")
public class BookingCartController {
    private BookingCartService bookingCartService;

    public BookingCartController(BookingCartService bookingCartService) {
        this.bookingCartService = bookingCartService;
    }

    @GetMapping("/{customerId}")
    public String viewBookingCart(@PathVariable Long customerId, Model model)   {
        model.addAttribute("customerId", customerId);
        Optional<BookingCart> cartOptional = bookingCartService.findBookingCartByCustomerId(customerId);
        if(cartOptional.isEmpty()) return "/bookingcart/empty";
        BookingCart cart = cartOptional.get();
        model.addAttribute("cartItems", cart.getBookingCartItems());
        return "/bookingcart/view";
    }
}
