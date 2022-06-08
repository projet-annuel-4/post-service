package com.example.postservice.data.repository;

import com.example.postservice.data.entities.LikeEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Integer countLikeEntitiesByPostId(Long postId);
    LikeEntity findByPostAndUser(PostEntity post, UserEntity user);
    void deleteByPostAndUser(PostEntity post, UserEntity user);

}
