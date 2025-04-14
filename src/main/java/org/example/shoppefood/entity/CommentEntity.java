package org.example.shoppefood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double rating;
	private String content;
	@Temporal(TemporalType.DATE)
	private Date rateDate;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "productId")
	private ProductEntity product;

	@OneToOne
	@JoinColumn(name = "orderDetailId")
	private OrderDetailEntity orderDetail;

}
