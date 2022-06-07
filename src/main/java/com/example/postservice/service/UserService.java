package com.example.postservice.service;

import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getById(Long userId){
        return userRepository.getById(userId);
    }
}
