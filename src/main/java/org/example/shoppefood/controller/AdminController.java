package org.example.shoppefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/home")
    public String home() {
        return "/admin/index";
    }
    @GetMapping("/categories")
    public String categories() {
        return "/admin/categories";
    }
    @GetMapping("/products")
    public String products() {
        return "/admin/products";
    }
    @GetMapping("/orders")
    public String orders() {
        return "/admin/orders";
    }
    @GetMapping("/users")
    public String users() {
        return "/admin/users";
    }
    @GetMapping("/reports/{kinds}")
    public String reports(@PathVariable("kinds") String kinds, Model model) {
        // In ra để kiểm tra
        System.out.println("Loại báo cáo: " + kinds);
        // Thêm `kinds` vào model để sử dụng bên view nếu cần
        model.addAttribute("kinds", kinds);
        // Có thể render chung 1 view, rồi phân biệt dữ liệu theo "kinds"
        return "/admin/statistical";
    }

}
