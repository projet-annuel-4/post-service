package com.example.postservice.service;

import com.example.postservice.data.entities.FollowersEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.FollowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public List<FollowersEntity> getAllByUserId(Long userId){
        return followerRepository.findAllByUserId(userId);
    }

    public List<FollowersEntity> getSubscriptionsByUserId(Long userId){
        return followerRepository.findAllByFollowerId(userId);
    }



}
