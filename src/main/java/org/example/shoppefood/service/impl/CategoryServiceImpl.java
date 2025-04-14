package org.example.shoppefood.service.impl;

import org.example.shoppefood.dto.CategoryDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.entity.CategoryEntity;
import org.example.shoppefood.mapper.CategoryMapper;
import org.example.shoppefood.repository.CategoryRepository;
import org.example.shoppefood.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponsePage<List<CategoryDTO>> getAllCategory(Pageable pageable) {
        Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryEntity> categoryEntities = categoryPage.getContent();
        List<CategoryDTO> categoryDTOS = categoryMapper.entityToDtoList(categoryEntities);
        ResponsePage<List<CategoryDTO>> responsePage = new ResponsePage<>();
        responsePage.setContent(categoryDTOS);
        responsePage.setPageNumber(categoryPage.getNumber());
        responsePage.setPageSize(categoryPage.getSize());
        responsePage.setTotalElements(categoryPage.getTotalElements());
        responsePage.setTotalPages(categoryPage.getTotalPages());

        return responsePage;
    }
}
