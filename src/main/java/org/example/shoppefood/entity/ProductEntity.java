package org.example.shoppefood.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String productName;
	private int quantity;
	private double price;
	private int discount;
	private String productImage;
	private String description;
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	private Boolean status;
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private CategoryEntity category;
	public boolean favorite;


}
