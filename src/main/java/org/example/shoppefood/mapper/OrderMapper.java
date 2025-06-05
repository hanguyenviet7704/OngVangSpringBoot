package org.example.shoppefood.mapper;

import org.example.shoppefood.dto.OrderDTO;
import org.example.shoppefood.entity.OrderEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    List<OrderDTO> toDTOs(List<OrderEntity> entities);
    OrderEntity toEntity(OrderDTO dto);
    List<OrderEntity> toEntities(List<OrderDTO> dtos);
    
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "user.userId", target = "userId")
    OrderDTO toDTO(OrderEntity entity);
}
