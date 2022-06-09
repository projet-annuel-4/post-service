package com.example.postservice.data.repository;

import com.example.postservice.data.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPostId(Long postId);
    void deleteAllByPostId(Long postId);

}
