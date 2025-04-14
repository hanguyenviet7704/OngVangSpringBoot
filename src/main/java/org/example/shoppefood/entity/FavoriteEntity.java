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
@Table(name = "favorites")
public class FavoriteEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long favoriteId;
	@ManyToOne()
	@JoinColumn(name = "userId")
	private UserEntity user;
	@ManyToOne()
	@JoinColumn(name = "productId")
	private ProductEntity product;
}
