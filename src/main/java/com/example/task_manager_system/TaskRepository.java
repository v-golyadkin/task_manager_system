package com.example.task_manager_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

    @Query("SELECT t FROM TaskEntity t WHERE t.category.id = :categoryId")
    List<TaskEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t FROM TaskEntity t WHERE t.completed = :completed")
    List<TaskEntity> findByCompleted(@Param("completed") Boolean completed);

    @Query("SELECT t FROM TaskEntity t WHERE " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(:completed IS NULL OR t.completed = :completed)")
    List<TaskEntity> findByCategoryIdAndCompleted(
            @Param("categoryId") Long categoryId,
            @Param("completed") Boolean completed);

}
