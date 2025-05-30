package org.example.shoppefood.service;

import org.example.shoppefood.dto.UserDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.springframework.data.domain.Pageable;

import javax.naming.NameNotFoundException;
import java.util.List;

public interface UserService {
    public ResponsePage <List<UserDTO>> getAllUsers(Pageable pageable);


}
