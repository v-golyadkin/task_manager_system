package com.example.task_manager_system;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record Task(
        Long id,
        String title,
        String description,
        Boolean completed,
        Category category,
        ZonedDateTime createdAt
) {

}
