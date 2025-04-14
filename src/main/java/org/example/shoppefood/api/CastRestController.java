package org.example.shoppefood.api;


import org.example.shoppefood.dto.cart.CartItemDTO;
import org.example.shoppefood.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@RequestMapping("api/cart")
@SessionAttributes("cart")
public class CastRestController {
    @Autowired
    ProductService productService;

    @ModelAttribute ("cart")
    public CartItemDTO cart(){
        return new CartItemDTO();
    }

    
}
