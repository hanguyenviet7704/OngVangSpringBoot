package org.example.shoppefood.service.impl;

import org.apache.catalina.User;
import org.example.shoppefood.dto.UserDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.RoleEntity;
import org.example.shoppefood.entity.UserEntity;
import org.example.shoppefood.mapper.UserMapper;
import org.example.shoppefood.repository.UserRepository;
import org.example.shoppefood.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponsePage<List<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = userMapper.toListDto(userPage.getContent());
        ResponsePage<List<UserDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(userDTOs);
        responsePage.setPageNumber(userPage.getNumber());
        responsePage.setPageSize(userPage.getSize());
        responsePage.setTotalElements(userPage.getTotalElements());
        responsePage.setTotalPages(userPage.getTotalPages());
        return responsePage;
    }
     @Override
     public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserDetails) {
            CustomUserDetailsService.CustomUserDetails userDetails = (CustomUserDetailsService.CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId(); // Lấy ID người dùng
        }
        return null;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }
        return userMapper.toDto(user);
    }
}
