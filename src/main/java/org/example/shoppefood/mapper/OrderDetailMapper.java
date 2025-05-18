package org.example.shoppefood.mapper;

import org.example.shoppefood.entity.OrderDetailEntity;
import org.example.shoppefood.dto.OrderDetailDTO;
import org.example.shoppefood.entity.ProductEntity;
import org.example.shoppefood.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "order.orderId", target = "orderId")
    OrderDetailDTO toDTO(OrderDetailEntity entity);

    @Mapping(source = "productId", target = "product.productId")
    @Mapping(source = "orderId", target = "order.orderId")
    OrderDetailEntity toEntity(OrderDetailDTO dto);
}
