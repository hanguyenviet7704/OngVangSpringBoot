package org.example.shoppefood.service;

import org.example.shoppefood.dto.UserDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public ResponsePage <List<UserDTO>> getAllUsers(Pageable pageable);
    public Long getCurrentUserId();

}
