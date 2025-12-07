package com.example.task_manager_system;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


@Service
public class TaskService
{

    private final TaskRepository taskRepository;

    private  final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository)
    {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task getTaskById(Long id)
    {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No found task by id: " + id));

        return toDomainTask(taskEntity);
    }

    public List<Task> getAllTasks()
    {

        List<TaskEntity> allEntity = taskRepository.findAll();

        List<Task> allTasks = allEntity.stream()
                .map(this::toDomainTask)
                .toList();

        return allTasks;
    }

    public List<Task> getTasksByCategory(Long categoryId)
    {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with id: " + categoryId);
        }

        List<TaskEntity> tasks = taskRepository.findByCategoryId(categoryId);

        return tasks.stream()
                .map(this::toDomainTask)
                .toList();
    }

    public Task createTask(Task taskToCreate)
    {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if(taskToCreate.completed()) {
            throw new IllegalArgumentException("Completed should be false");
        }

        CategoryEntity categoryEntity = null;
        if (taskToCreate.category() != null) {
            categoryEntity = categoryRepository.findById(taskToCreate.category().id())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + taskToCreate.category().id()));
        }

        var entityToSave = new TaskEntity(
                null,
                taskToCreate.title(),
                taskToCreate.description(),
                false,
                categoryEntity,
                ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
        );

        var savedEntity = taskRepository.save(entityToSave);

        return toDomainTask(savedEntity);
    }

    public void deleteTask(Long id)
    {
        if(!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("No found task by id: " + id);
        }

        taskRepository.deleteById(id);
    }

    public Task markAsCompleted(Long id)
    {

        var taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No found task by id: " + id));

        if(taskEntity.getCompleted()) {
            throw new IllegalArgumentException("Already task is completed by id: " + id);
        }

        var taskToUpdate = new TaskEntity(
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                true,
                taskEntity.getCategory(),
                taskEntity.getCreatedAt()
        );

        var updatedTask = taskRepository.save(taskToUpdate);

        return toDomainTask(updatedTask);
    }

    public List<Task> getTaskWithFilters(Long categoryId, Boolean completed)
    {
        List<TaskEntity> tasks = taskRepository.findByCategoryIdAndCompleted(categoryId, completed);

        return tasks.stream()
                .map(this::toDomainTask)
                .toList();
    }

    private Task toDomainTask(TaskEntity task)
    {
        return new Task(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                toDomainCategory(task.getCategory()),
                task.getCreatedAt()
        );
    }

    private Category toDomainCategory(CategoryEntity category)
    {
        return new Category(category.getId(), category.getName());
    }
}
