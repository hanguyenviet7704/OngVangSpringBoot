package org.example.shoppefood.service;

import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    ResponsePage<List<OrderDTO>> getAllOrders(Pageable pageable);
    ResponsePage<List<OrderDTO>> getOrdersByUserID(Pageable pageable, long userID);
    
    // Thêm các phương thức thanh toán
    OrderDTO createOrder(Long userId, String address, String phone);
    OrderDTO getOrderById(Long orderId);
    OrderDTO updateOrderStatus(Long orderId, int status);
    void cancelOrder(Long orderId);
}
