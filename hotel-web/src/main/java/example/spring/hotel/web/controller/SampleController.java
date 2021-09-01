package example.spring.hotel.web.controller;

import example.spring.hotel.application.product.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @Autowired
    private ProductManager productManager;

    @GetMapping("/hello")
    public String helll()   {
        productManager.deleteProduct(null);
        return "hello";
    }
}
