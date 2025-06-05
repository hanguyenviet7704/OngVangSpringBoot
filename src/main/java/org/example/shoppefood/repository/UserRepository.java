package org.example.shoppefood.repository;

import org.example.shoppefood.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(Long userId);
    Page<UserEntity> findAll(Pageable pageable);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
}
