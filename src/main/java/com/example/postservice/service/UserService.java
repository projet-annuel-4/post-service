package com.example.postservice.service;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.UserRepository;
import com.example.postservice.domain.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserEntity createUser(UserEvent userEvent){
        return userRepository.save(userMapper.eventToEntity(userEvent));
    }

    public Optional<UserEntity> getById(Long userId){
        return userRepository.findById(userId);
    }


}
