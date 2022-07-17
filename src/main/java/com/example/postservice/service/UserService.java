package com.example.postservice.service;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.FollowerRepository;
import com.example.postservice.data.repository.UserRepository;
import com.example.postservice.data.request.UserRequest;
import com.example.postservice.domain.mapper.UserMapper;
import com.example.postservice.domain.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FollowerRepository followerRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.followerRepository = followerRepository;
    }

    public UserEntity createUser(UserEvent userEvent){
        return userRepository.save(userMapper.eventToEntity(userEvent));
    }

    public Optional<UserEntity> getById(Long userId){
        return userRepository.findById(userId);
    }

    public UserModel update(Long userId, UserRequest userRequest){
        var user = getById(userId).get()
                .setFirstname(userRequest.getFirstname())
                .setLastname(userRequest.getLastname())
                .setEmail(userRequest.getEmail())
                .setNbFollowers(followerRepository.countAllByUserId(userId))
                .setNbSubscription(followerRepository.countAllByFollowerId(userId));

        userRepository.save(user);

        return userMapper.entityToModel(user);
    }


}
