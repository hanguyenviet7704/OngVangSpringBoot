package org.example.shoppefood.api;
import org.example.shoppefood.dto.CategoryDTO;
import org.example.shoppefood.dto.responsePage.ResponsePage;
import org.example.shoppefood.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api")
public class CategoryAPI {
    @Autowired
    private CategoryService categoryService;
    @GetMapping ("/categoris")
    public ResponsePage<List<CategoryDTO>> getAllCategories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam (defaultValue = "100") int size
    ) {
            Pageable pageable = PageRequest. of (page, size);
            return categoryService.getAllCategory(pageable);
    }
}