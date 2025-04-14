package org.example.shoppefood.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemEntity {

	private Long id;
	private String name;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private ProductEntity product;
}
