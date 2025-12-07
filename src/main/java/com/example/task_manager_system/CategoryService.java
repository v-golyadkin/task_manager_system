package com.example.task_manager_system;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category categoryToCreate) {

        if(categoryToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }

        var entityToSave = new CategoryEntity(null , categoryToCreate.name());

        var savedEntity = repository.save(entityToSave);

        return toDomainCategory(savedEntity);
    }

    public List<Category> getAllCategories()
    {

        List<CategoryEntity> allEntity = repository.findAll();

        List<Category> allCategories = allEntity.stream()
                .map(this::toDomainCategory)
                .toList();

        return allCategories;
    }

    private Category toDomainCategory(CategoryEntity category){
        return new Category(
                category.getId(),
                category.getName()
        );
    }
}
