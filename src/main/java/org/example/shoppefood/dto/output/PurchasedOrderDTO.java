package org.example.shoppefood.dto.output;

import lombok.Data;
import lombok.Setter;

@Data


public class PurchasedOrderDTO {
    private String ProductImage;
    private String ProductName;
    private double ProductPrice;
    private int ProductQuantity;
    private double TotalAmount;
}
