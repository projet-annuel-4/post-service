package com.example.postservice.data.repository;

import com.example.postservice.data.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    void deleteAllByPostId(Long postId);

}
