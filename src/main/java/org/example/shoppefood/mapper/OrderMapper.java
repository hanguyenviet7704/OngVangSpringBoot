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
}
