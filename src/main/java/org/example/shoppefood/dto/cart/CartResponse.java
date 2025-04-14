package org.example.shoppefood.dto.cart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter
public class CartResponse {
    private List<CartItemDTO> items;
    private double total;
}
