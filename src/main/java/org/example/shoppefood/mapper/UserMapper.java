package org.example.shoppefood.mapper;

import org.example.shoppefood.dto.RoleDTO;
import org.example.shoppefood.dto.UserDTO;
import org.example.shoppefood.entity.RoleEntity;
import org.example.shoppefood.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserDTO toDto(UserEntity entity);
    UserEntity toEntity(UserDTO dto);
    List<UserDTO> toListDto(List<UserEntity> entities);
    @Named("roleToDto")
    RoleDTO roleToDto(RoleEntity entity);
    @Named("roleToEntity")
    RoleEntity roleToEntity(RoleDTO dto);
}