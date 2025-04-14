package org.example.shoppefood.mapper;

import org.example.shoppefood.dto.CategoryDTO;
import org.example.shoppefood.entity.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO entityToDto(CategoryEntity categoryEntity);
    CategoryEntity dtoToEntity(CategoryDTO categoryDTO);
    List<CategoryDTO> entityToDtoList(List<CategoryEntity> categoryEntities);
    List<CategoryEntity> dtoToEntityList(List<CategoryDTO> categoryDTOs);
}
