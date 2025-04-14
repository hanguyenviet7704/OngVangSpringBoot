package org.example.shoppefood.repository;

import org.example.shoppefood.dto.output.PurchasedOrderDTO;
import org.example.shoppefood.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAll(Pageable pageable);
    ProductEntity findByProductId(int productId);

    @Query("SELECT p FROM ProductEntity p WHERE p.category.categoryId = :categoryId")
    Page<ProductEntity> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT h FROM ProductEntity h WHERE (:keyword IS NULL OR :keyword = '' OR h.productName LIKE %:keyword%)")
    Page<ProductEntity> searchByNameLike(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT p FROM ProductEntity p WHERE (:categoryId IS NULL OR p.category.categoryId = :categoryId)")
    Page<ProductEntity> searchByCategoryId(Pageable pageable, @Param("categoryId") Long categoryId);

    @Query("SELECT p.category.categoryName, COUNT(p) FROM ProductEntity p GROUP BY p.category.categoryName")
    List<Object[]> countProductsByCategory();
    @Query("SELECT p.productImage AS productImage, p.productName AS productName, p.price AS productPrice, od.quantity AS productQuantity, (od.quantity * (p.price - p.discount)) AS totalAmount " +
            "FROM OrderDetailEntity od JOIN ProductEntity p ON od.product.productId = p.productId " +
            "WHERE od.order.orderId = :orderId")
    List<Object[]> findPurchasedProductsByOrderId(@Param("orderId") Long orderId);
}
