package org.example.shoppefood.dto.cart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartItemDTO {
    private Long productId;
    private int quantity;
}
