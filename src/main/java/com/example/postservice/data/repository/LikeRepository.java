package com.example.postservice.data.repository;

import com.example.postservice.data.entities.LikeEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Integer countLikeEntitiesByPostId(Long postId);
    LikeEntity findByPostAndUser(PostEntity post, UserEntity user);
    Optional<List<LikeEntity>> findLikeEntitiesByPostId(Long postId);
    Optional<List<LikeEntity>> findLikeEntitiesByUserId(Long userId);
    void deleteByPostAndUser(PostEntity post, UserEntity user);
    void deleteAllByPostId(Long postId);

}
