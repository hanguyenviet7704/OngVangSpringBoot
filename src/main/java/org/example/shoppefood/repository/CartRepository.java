package org.example.shoppefood.repository;

import org.example.shoppefood.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("SELECT c FROM CartEntity c WHERE c.user.userId = :userID ")
    Optional<CartEntity> findByUserId(Long userID);
} 