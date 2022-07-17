package com.example.postservice.service;

import com.example.postservice.data.entities.FollowersEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.FollowerRepository;
import com.example.postservice.domain.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public FollowerService(FollowerRepository followerRepository, UserService userService, UserMapper userMapper) {
        this.followerRepository = followerRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public List<FollowersEntity> getAllByUserId(Long userId){
        return followerRepository.findAllByUserId(userId);
    }

    public FollowersEntity getByFollowerAndUser(UserEntity follower, UserEntity user){
        return followerRepository.findByFollowerIdAndUserId(follower.getId(), user.getId());
    }

    public List<FollowersEntity> getSubscriptionsByUserId(Long userId){
        return followerRepository.findAllByFollowerId(userId);
    }

    public void follow(UserEntity follower, UserEntity user){
        var followLink = new FollowersEntity()
                .setUser(user)
                .setFollower(follower);
        followerRepository.save(followLink);

        userService.update(user.getId(), userMapper.entityToRequest(user));
        userService.update(follower.getId(), userMapper.entityToRequest(follower));
    }

    @Transactional
    public void unfollow(UserEntity follower, UserEntity user){
        followerRepository.deleteByFollowerIdAndAndUserId(follower.getId(), user.getId());
        userService.update(user.getId(), userMapper.entityToRequest(user));
        userService.update(follower.getId(), userMapper.entityToRequest(follower));
    }



}
