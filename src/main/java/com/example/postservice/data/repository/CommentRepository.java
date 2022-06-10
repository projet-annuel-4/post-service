package com.example.postservice.data.repository;

import com.example.postservice.data.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPostId(Long postId);
    void deleteAllByPostId(Long postId);

}
