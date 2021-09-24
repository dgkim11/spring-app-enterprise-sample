package example.spring.hotel.web.controller;

import example.spring.hotel.application.checkout.CheckoutService;
import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentService;
import example.spring.hotel.domain.model.payment.exception.PaymentException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;
    private CheckoutService checkoutService;

    public PaymentController(PaymentService paymentService, CheckoutService checkoutService) {
        this.paymentService = paymentService;
        this.checkoutService = checkoutService;
    }

    @PostMapping("/execute")
    public String executePay(@RequestBody PaymentRequest paymentRequest, Model model)  {
        try {
            Payment payment = paymentService.pay(
                    paymentRequest.getGatewayCompanyId(),
                    checkoutService.findById(paymentRequest.getCheckoutId()),
                    paymentRequest.getPaymentInfoList()
            );
            model.addAttribute("payment", payment);

            return "/payment/view";
        } catch (PaymentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
