package org.example.shoppefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value ="/login")
    public String login() {
        return "web/login.html";
    }
  @GetMapping(value = "register")
  public String register() {
        return "web/register.html";
  }
  @GetMapping("/forgotPassword")
  public String forgotPassword() {
        return "web/forgotPassword.html";
  }
    @GetMapping(value = "/home")
    public String home (){
        return "web/home.html";
    }
    @GetMapping(value = "/aboutUs")
    public String aboutUs (){
        return "web/about.html";
    }
    @GetMapping(value = "/contact" )
    public String contact (){
        return "web/contact.html";
    }
    @GetMapping(value = "/products")
    public String products (){
        return "web/shop.html";
    }
    @GetMapping(value = "/favorite")
    public String favourite(){
        return "web/favorite.html";
    }
}
