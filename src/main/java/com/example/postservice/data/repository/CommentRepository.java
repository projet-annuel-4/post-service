package com.example.postservice.data.repository;

import com.example.postservice.data.entities.CommentEntity;
import com.example.postservice.data.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPostId(Long postId);
    List<CommentEntity> findAllByUserId(Long postId);
    void deleteAllByPost(PostEntity postEntity);

}
