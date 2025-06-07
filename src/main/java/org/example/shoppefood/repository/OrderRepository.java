package org.example.shoppefood.repository;


import org.example.shoppefood.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAll(Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.userId = :userID ")
    Page<OrderEntity> findAllOrderByUserId(long userID,Pageable pageable);

}
