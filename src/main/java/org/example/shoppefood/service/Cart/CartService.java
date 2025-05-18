package org.example.shoppefood.service.Cart;

import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.cart.CartItemDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class CartService {
    private List<CartItemDTO> items = new ArrayList<>();

    public List<CartItemDTO> getAllItems() {
        return items;
    }

    public void addToCart(ProductDTO productDTO , int quantity) {
         for (CartItemDTO item : items) {
               if (item.getProduct().getProductId().equals(productDTO.getProductId())) {
                   item.setQuantity(item.getQuantity() + quantity);
                   return ;
               }
         }
         CartItemDTO newItem = new CartItemDTO();
         newItem.setQuantity(quantity);
         newItem.setProduct(productDTO);
         items.add(newItem);
    }
    public void removeFromCart(Long productId) {
       for (CartItemDTO item : items) {
           if (item.getProduct().getProductId().equals(productId)) {
               items.remove(item);
           }
       }
    }
    public double getTotal() {
       double total = 0;
        for (CartItemDTO item : items) {
            total = 0;
            total += item.getQuantity()*item.getProduct().getPrice();
        }
        return total;
    }
    public void clearCart() {
        items.clear();
    }
}
