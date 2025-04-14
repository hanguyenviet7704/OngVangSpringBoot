package org.example.shoppefood.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long orderDetailId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Long orderId;
}