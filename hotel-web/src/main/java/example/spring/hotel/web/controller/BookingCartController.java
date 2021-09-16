package example.spring.hotel.web.controller;

import example.spring.hotel.application.bookingcart.BookingCartService;
import example.spring.hotel.domain.model.bookingcart.BookingCart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class BookingCartController {
    private BookingCartService bookingCartService;

    public BookingCartController(BookingCartService bookingCartService) {
        this.bookingCartService = bookingCartService;
    }

    @GetMapping("/{customerId}")
    public String cartPage(@PathVariable Long customerId, Model model)   {
        Optional<BookingCart> cartOptional = bookingCartService.findBookingCartByCustomerId(customerId);
        if(cartOptional.isEmpty()) return "/cart/empty";
        BookingCart cart = cartOptional.get();
        model.addAttribute("customerId", cart.getCustomerId());
        model.addAttribute("cartItems", cart.getBookingCartItems());
        return "/cart/view";
    }
}
