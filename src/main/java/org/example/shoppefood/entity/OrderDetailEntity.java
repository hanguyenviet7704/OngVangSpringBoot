package org.example.shoppefood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderDetails")
public class OrderDetailEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderDetailId;
	private int quantity;
	private Double price;
	
	@ManyToOne
	@JoinColumn(name = "productId")
	private ProductEntity product;
	
	@ManyToOne
	@JoinColumn(name = "orderId")
	private OrderEntity order;
}
