package org.example.shoppefood.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private int quantity;
    private double price;
    private double subtotal;
} 