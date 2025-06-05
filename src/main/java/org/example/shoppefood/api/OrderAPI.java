package org.example.shoppefood.api;
import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String phone) {
        return ResponseEntity.ok(orderService.createOrder(userId, address, phone));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam int status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }


    // Thêm các endpoint mới cho admin
    @PutMapping("/order/confirm/{orderId}")
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, 1)); // CONFIRMED
    }

    @PutMapping("order/delivered/{orderId}")
    public ResponseEntity<OrderDTO> markOrderAsDelivered(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, 2)); // DELIVERED
    }
    @PutMapping("/order/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, 3)); // CONFIRMED
    }



}
