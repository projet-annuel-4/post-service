package com.example.postservice.data.repository;


import com.example.postservice.data.entities.FollowersEntity;
import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<FollowersEntity, Long> {

    List<FollowersEntity> findAllByUserId(Long userId);

    /*
    @Query("SELECT FollowersEntity.user FROM FollowersEntity f WHERE f.follower.id = :userId")
    List<UserEntity> findSubscriptionsByUserId(@Param("userId") Long userId);

     */

    List<FollowersEntity> findAllByFollowerId(Long followerId);
    FollowersEntity findByFollowerIdAndUserId(Long followerId, Long userId);
    Integer countAllByUserId(Long userId);
    Integer countAllByFollowerId(Long userId);
    void deleteByFollowerIdAndAndUserId(Long followerId, Long userId);



}
