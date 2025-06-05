package org.example.shoppefood.service.impl;

import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.*;
import org.example.shoppefood.mapper.OrderMapper;
import org.example.shoppefood.repository.*;
import org.example.shoppefood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public ResponsePage<List<OrderDTO>> getAllOrders(Pageable pageable) {
        Page<OrderEntity> orderPage = orderRepository.findAll(pageable);
        List<OrderDTO> orderDTOs = orderMapper.toDTOs(orderPage.getContent());
        ResponsePage<List<OrderDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(orderDTOs);
        responsePage.setPageNumber(orderPage.getNumber());
        responsePage.setPageSize(orderPage.getSize());
        responsePage.setTotalElements(orderPage.getTotalElements());
        responsePage.setTotalPages(orderPage.getTotalPages());
        return responsePage;
    }

    @Override
    public ResponsePage<List<OrderDTO>> getOrdersByUserID(Pageable pageable, long userID) {
        Page<OrderEntity> orderPage = orderRepository.findAllOrderByUserId(userID, pageable);
        List<OrderDTO> orderDTOs = orderMapper.toDTOs(orderPage.getContent());
        ResponsePage<List<OrderDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(orderDTOs);
        responsePage.setPageNumber(orderPage.getNumber());
        responsePage.setPageSize(orderPage.getSize());
        responsePage.setTotalElements(orderPage.getTotalElements());
        responsePage.setTotalPages(orderPage.getTotalPages());
        return responsePage;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, String address, String phone) {
        // Lấy thông tin user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy giỏ hàng của user
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        // Tạo đơn hàng mới
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setAddress(address);
        order.setPhone(phone);
        order.setStatus(0); // 0: Chờ xác nhận
        order.setAmount(cart.getTotalAmount());

        // Chuyển các sản phẩm từ giỏ hàng sang chi tiết đơn hàng
        OrderEntity finalOrder = order;
        List<OrderDetailEntity> orderDetails = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderDetailEntity orderDetail = new OrderDetailEntity();
                    orderDetail.setOrder(finalOrder);
                    orderDetail.setProduct(cartItem.getProduct());



                  // Nếu số lượng không đủ thì không cho đặt hàng.
                    int  quantityOrder = cartItem.getQuantity();
                    int inventoryQuantity =productRepository.getQuantity(cartItem.getProduct().getProductId());

                    if (quantityOrder < inventoryQuantity){
                        orderDetail.setQuantity(quantityOrder);
                        productRepository.updateQuantityByProductId(orderDetail.getProduct().getProductId(),inventoryQuantity - quantityOrder);
                    } else{
                        throw new RuntimeException("Insufficient products to deliver");
                    }
                    orderDetail.setPrice(cartItem.getPrice());
                    return orderDetail;
                })
                .toList();
        order.setOrderDetails(orderDetails);

        // Lưu đơn hàng
        order = orderRepository.save(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cartRepository.delete(cart);

        return orderMapper.toDTO(order);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, int status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        order = orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Chỉ cho phép hủy đơn hàng khi đang ở trạng thái chờ xác nhận
        if (order.getStatus() != 0) {
            throw new RuntimeException("Cannot cancel order in current status");
        }
        
        order.setStatus(4); // 4: Đã hủy
        orderRepository.save(order);
    }
}
