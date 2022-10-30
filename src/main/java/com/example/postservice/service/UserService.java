package com.example.postservice.service;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.FollowerRepository;
import com.example.postservice.data.repository.UserRepository;
import com.example.postservice.data.request.UserRequest;
import com.example.postservice.domain.mapper.UserMapper;
import com.example.postservice.domain.model.UserModel;
import com.example.postservice.util.Levenshtein;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FollowerRepository followerRepository;
    private final Levenshtein levenshtein;

    public UserService(UserRepository userRepository, UserMapper userMapper, FollowerRepository followerRepository, Levenshtein levenshtein) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.followerRepository = followerRepository;
        this.levenshtein = levenshtein;
    }

    public UserEntity createUser(UserEvent userEvent){
        System.out.println(userEvent);
        var user = userMapper.eventToEntity(userEvent);
        return userRepository.save(user);
    }

    public Optional<UserEntity> getById(Long userId){
        return userRepository.findById(userId);
    }


    public List<UserModel> getByFirstname(String firstname){
        var users = userRepository.findAll();
        var usersFound = new ArrayList<UserEntity>();
        users.forEach(user -> {
            if(levenshtein.calculate(firstname.toUpperCase(), user.getFirstName().toUpperCase()) < 3){
                usersFound.add(user);
            }
        });

        return usersFound
                .stream()
                .map(UserMapper::entityToModel)
                .collect(toList());
    }

    public UserModel getByEmail(String email){
        var user = userRepository.findByEmail(email);
        return UserMapper.entityToModel(user);
    }

    public UserModel update(Long userId, UserRequest userRequest){
        var user = getById(userId).get()
                .setFirstName(userRequest.getFirstName())
                .setLastName(userRequest.getLastName())
                .setEmail(userRequest.getEmail())
                .setNbFollowers(followerRepository.countAllByUserId(userId))
                .setNbSubscription(followerRepository.countAllByFollowerId(userId));

        userRepository.save(user);

        return UserMapper.entityToModel(user);
    }

    public List<UserModel> getAllFollowers(Long userId){
        var followerLink = followerRepository.findAllByUserId(userId);

        var followers = new ArrayList<UserEntity>();
        followerLink.forEach(link -> {
            var user = getById(link.getFollower().getId());
            user.ifPresent(followers::add);
        });

        return followers
                .stream()
                .map(UserMapper::entityToModel)
                .collect(toList());
    }

    public List<UserModel> getAllSubscriptions(Long userId){
        var followerLink = followerRepository.findAllByFollowerId(userId);

        var subscriptions = new ArrayList<UserEntity>();
        followerLink.forEach(link -> {
            var user = getById(link.getUser().getId());
            user.ifPresent(subscriptions::add);
        });

        return subscriptions
                .stream()
                .map(UserMapper::entityToModel)
                .collect(toList());
    }

}
