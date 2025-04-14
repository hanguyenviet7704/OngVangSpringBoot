package org.example.shoppefood.service.impl;

import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.OrderEntity;
import org.example.shoppefood.mapper.OrderMapper;
import org.example.shoppefood.repository.OrderRepository;
import org.example.shoppefood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
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
}
