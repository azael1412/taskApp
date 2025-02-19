package com.azael.taskapp.persistence.repositories;

import org.springframework.stereotype.Repository;

import com.azael.taskapp.persistence.entities.Task;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tasks WHERE user_id = :userId AND name = :name", nativeQuery = true)
    boolean existsByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);
    boolean existsByUserIdAndNameAndIdNot(Long userId, String name, Long id);
    Page<Task> findByNameLike(String name, Pageable pageable);
    Page<Task> findByUserId(Pageable pageable, Long userId);
    
    @Query("SELECT t FROM Task t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.user.id = :userId")
    Page<Task> findByNameLikeAndUserId(@Param("name") String name, Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    Page<Task> findAllAndUserId(Pageable pageable, @Param("userId") Long userId);
    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.user.id = :userId")
    Optional<Task> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}