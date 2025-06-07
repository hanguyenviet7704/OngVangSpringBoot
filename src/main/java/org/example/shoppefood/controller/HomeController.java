package org.example.shoppefood.controller;

import org.example.shoppefood.service.OrderService;
import org.example.shoppefood.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/login")
    public String login() {
        return "web/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "web/register.html";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "web/forgotPassword";
    }

    @GetMapping("/home")
    public String home() {
        return "web/home";
    }

    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "web/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "web/contact.html";
    }

    @GetMapping("/products")
    public String products() {
        return "web/shop.html";
    }

    @GetMapping("/favorite")
    public String favourite() {
        return "web/favorite.html";
    }

    @GetMapping({"/productsByCategory/{id}"})
    public String productsByCategory(@PathVariable(required = false) Long id, Model model) {
        if (id == null || id == 0) {
            model.addAttribute("categoryId", null);
        } else {
            model.addAttribute("categoryId", id);
        }
        return "web/shop.html";
    }

    @GetMapping({"/productDetail/{id}"})
    public String productDetail(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("product", id);
        return "web/productDetail.html";
    }

    @GetMapping({"/checkout"})
    public String checkout() {
        return "web/shoppingCart_checkout";
    }

    @GetMapping({"/checkout_success"})
    public String checkout_success() {
        return "web/checkout_success";
    }

    @GetMapping({"/checkout_paypal_success"})
    public String checkout_paypal_success() {
        return "web/checkout_paypal_success";
    }
    @GetMapping({"/checkout_paypal_lose"})
    public String not_found(){
        return "web/checkout_paypal_lose";
    }
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayPalService payPalService;

    @GetMapping("/orders/paypal/success")
    public String paypalSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("orderId") Long orderId) {
        try {
            payPalService.executePayment(paymentId, payerId);
            orderService.updateOrderStatus(orderId, 1);
            return "redirect:/checkout_paypal_success";
        } catch (Exception e) {
            return "redirect:/error?message=" + e.getMessage();
        }
    }

    @GetMapping("/orders/paypal/cancel")
    public String paypalCancel(@RequestParam("orderId") Long orderId) {
        try {
            orderService.updateOrderStatus(orderId, 3);
            return "redirect:/checkout_paypal_lose";
        } catch (Exception e) {
            return "redirect:/error?message=" + e.getMessage();
        }
    }

}
