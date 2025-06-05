package org.example.shoppefood.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private int discount;
    private String productImage;
    private String description;
    private Date enteredDate;
    private Boolean status;
    public boolean favorite;
    private Long categoryId;
    private String categoryName;
}
