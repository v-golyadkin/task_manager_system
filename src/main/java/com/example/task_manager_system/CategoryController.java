package com.example.task_manager_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category categoryToCreate
    ) {
        log.info("Called createCategory");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryToCreate));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Called getAllCategories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
