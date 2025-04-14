package org.example.shoppefood.mapper;

import org.example.shoppefood.dto.RoleDTO;
import org.example.shoppefood.entity.RoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(RoleEntity roleEntity);
    RoleEntity toEntity(RoleDTO roleDTO);
    List<RoleDTO> toDTOs(List<RoleEntity> entities);
    List<RoleEntity> toEntities(List<RoleDTO> dtos);
}
