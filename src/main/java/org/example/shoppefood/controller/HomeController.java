package org.example.shoppefood.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping({ "/productsByCategory/{id}"})
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
}
