package com.example.postservice.data.repository;

import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity getById(Long id);
}
