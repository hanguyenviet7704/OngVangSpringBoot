package org.example.shoppefood.mapper;

import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.entity.ProductEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")  // componentModel = "spring" giúp MapStruct tạo bean Spring cho Mapper
public interface ProductMapper {

   @Mapping(source = "category.categoryId", target = "categoryId") // Chuyển categoryId từ CategoryEntity
   @Mapping(source = "category.categoryName", target = "categoryName") // Map categoryName từ CategoryEntity
   @Mapping(source = "enteredDate", target = "enteredDate") // Add mapping for enteredDate
   ProductDTO entityToDto(ProductEntity productEntity);

   List<ProductDTO> entityToDtoList(List<ProductEntity> productEntities);
   @Mapping(source = "categoryId", target = "category.categoryId") // Chuyển categoryId về CategoryEntity
   @Mapping(source = "enteredDate", target = "enteredDate") // Add mapping for enteredDate
   ProductEntity dtoToEntity(ProductDTO productDTO);
}
