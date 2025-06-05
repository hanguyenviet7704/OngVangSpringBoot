package org.example.shoppefood.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {
    private String address;
    private String phone;
    private String note;

    // Getters và Setters
}
