package com.example.postservice.data.repository;

import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity getById(Long id);
    UserEntity findByEmail(String email);
}
