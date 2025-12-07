package com.example.task_manager_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);


    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") Long id
    ) {
        log.info("Called getTaskById with id: " + id);
        return  ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Task>> getTasksByCategory(
            @PathVariable("categoryId") Long categoryId
    ){
        log.info("Called getTasksByCategory");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasksByCategory(categoryId));
    }

    @PostMapping()
    public ResponseEntity<Task> createTask(
            @RequestBody Task taskToCreate
    ){
        log.info("Called createTask");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteTask with id: {} ", id);
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsComplete(
            @PathVariable("id") Long id,
            @RequestBody Task taskToUpdate
    ){
        log.info("Called markTaskAsComplete");
        var updated = taskService.markAsCompleted(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean completed ) {
        if (category != null || completed != null) {
            log.info("Called getTasksByCategory with category id: {}", category);
            List<Task> tasks = taskService.getTaskWithFilters(category, completed);
            return ResponseEntity.ok(tasks);
        } else {
            log.info("Called getAllTasks");
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        }
    }
}
