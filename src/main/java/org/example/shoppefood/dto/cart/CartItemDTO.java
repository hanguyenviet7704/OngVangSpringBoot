package org.example.shoppefood.dto.cart;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.shoppefood.dto.ProductDTO;
@Data
@Getter
@Setter
public class CartItemDTO {
    private ProductDTO product;
    private int quantity;
    public double getTotalPrice (){
        return product.getPrice() * quantity;
    }
}
