package org.example.shoppefood.service;

import org.example.shoppefood.dto.CategoryDTO;
import org.example.shoppefood.dto.ProductDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    ResponsePage<List<CategoryDTO>> getAllCategory(Pageable pageable);
    
    CategoryDTO save(CategoryDTO categoryDTO);
    
    void deleteById(Long id);
    
    CategoryDTO findById(Long id);
}
