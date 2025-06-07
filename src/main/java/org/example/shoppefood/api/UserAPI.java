package org.example.shoppefood.api;

import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.UserDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.service.OrderService;
import org.example.shoppefood.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserAPI {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public ResponsePage<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest. of (page, size);
        return userService.getAllUsers(pageable);
    }
    @GetMapping("/users/now")
    public Long getUserIDNow (){
        return userService.getCurrentUserId();
    }
    
    @GetMapping("/users/current")
    public UserDTO getCurrentUser() {
        return userService.getUserById(userService.getCurrentUserId());
    }
}