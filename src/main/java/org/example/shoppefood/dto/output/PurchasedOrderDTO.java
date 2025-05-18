package org.example.shoppefood.dto.output;
import lombok.Data;
@Data
public class PurchasedOrderDTO {
    private String ProductImage;
    private String ProductName;
    private double ProductPrice;
    private int ProductQuantity;
    private double TotalAmount;
}
