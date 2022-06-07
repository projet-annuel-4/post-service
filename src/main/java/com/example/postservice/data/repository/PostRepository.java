package com.example.postservice.data.repository;


import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    PostEntity getById(Long id);
    List<PostEntity> getAllByUser(UserEntity user);
}
