package com.azael.taskapp.persistence.repositories;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.azael.taskapp.persistence.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //User findByEmailOrUserName(String email, String username);

    Optional<User> findByUsername(String userName);
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByUsernameAndIsActive(String username, boolean isActive);
    Optional<User> findByEmailAndIsActive(String email, boolean isActive);
    //Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);
    // List<User> findAllByOrderByAudit_CreatAtDesc();
    // Boolean findByUsernameAndIdIsActive(String username, Boolean isActive);
    User findByEmailAndIdNot(String email, Long id);
    User findByUsernameAndIdNot(String username, Long id);

    Page<User> findByNameLikeOrEmailLike(
        String name,
        String email,
        Pageable pageable
    );
    

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

   @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.id != :id")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("id") Long id);
}
