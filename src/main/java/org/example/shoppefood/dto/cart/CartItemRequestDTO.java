package org.example.shoppefood.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDTO {
    int productId;
    int quantity;
}
