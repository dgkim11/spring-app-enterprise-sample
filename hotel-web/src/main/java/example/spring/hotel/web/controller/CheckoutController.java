package example.spring.hotel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.spring.hotel.application.checkout.CheckoutRequestItem;
import example.spring.hotel.application.checkout.CheckoutService;
import example.spring.hotel.domain.model.checkout.Checkout;
import example.spring.hotel.domain.model.checkout.exception.CheckoutException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService)  {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/{customerId}")
    public String createCheckout(@PathVariable Long customerId, @RequestBody List<CheckoutRequestItem> requestItems, Model model)  {

        try {
            Checkout checkout = checkoutService.checkout(customerId, requestItems);
            model.addAttribute("checkout", checkout);
            return "checkout/view";
        } catch (CheckoutException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{checkoutId}")
    public String findCheckoutById(@PathVariable Long checkoutId, Model model)  {
        Checkout checkout = checkoutService.findById(checkoutId);
        model.addAttribute("checkout", checkout);
        return "checkout/view";
    }
}
