package org.example.shoppefood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Date orderDate;
    private Double amount;
    private String address;
    private String phone;
    private int status;
    private Long userId;
    private List<OrderDetailDTO> orderDetails;
}