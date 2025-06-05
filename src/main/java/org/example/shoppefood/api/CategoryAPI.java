package org.example.shoppefood.api;

import org.example.shoppefood.dto.CategoryDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryAPI {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoris")
    public ResponsePage<List<CategoryDTO>> getAllCategories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getAllCategory(pageable);
    }

    @GetMapping("/categoris/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping("/categoris")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.save(categoryDTO));
    }

    @PutMapping("/categoris/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(id);
        return ResponseEntity.ok(categoryService.save(categoryDTO));
    }

    @DeleteMapping("/categoris/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}