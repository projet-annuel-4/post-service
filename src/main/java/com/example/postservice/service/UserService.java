package com.example.postservice.service;

import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getById(Long userId){
        return userRepository.findById(userId);
    }


}
