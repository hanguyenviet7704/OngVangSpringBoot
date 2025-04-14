package org.example.shoppefood.api;
import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class OrderAPI {
    @Autowired
    private OrderService orderService;
    @GetMapping("/orders")
    public ResponsePage<List<OrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest. of (page, size);
        return orderService.getAllOrders(pageable);
    }
    @GetMapping("/orders/{id}")
    public ResponsePage<List<OrderDTO>> getAllOrdersByUserID(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam (defaultValue = "100") int size
    ){
        Pageable pageable = PageRequest. of (page, size);
        return orderService.getOrdersByUserID(pageable, id);
    }
}
