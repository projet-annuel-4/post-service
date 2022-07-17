package com.example.postservice.domain.mapper;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.request.UserRequest;
import com.example.postservice.domain.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity eventToEntity(UserEvent userEvent){
        return new UserEntity()
                .setFirstname(userEvent.getFirstName())
                .setLastname(userEvent.getLastName())
                .setEmail(userEvent.getEmail());
    }

    public UserModel entityToModel(UserEntity userEntity){
        return new UserModel()
                .setFirstname(userEntity.getFirstname())
                .setLastname(userEntity.getLastname())
                .setEmail(userEntity.getEmail())
                .setNbFollowers(userEntity.getNbFollowers())
                .setNbSubscription(userEntity.getNbSubscription());
    }

    public UserRequest entityToRequest(UserEntity userEntity){
        return new UserRequest()
                .setFirstname(userEntity.getFirstname())
                .setLastname(userEntity.getLastname())
                .setEmail(userEntity.getEmail())
                .setNbFollowers(userEntity.getNbFollowers())
                .setNbSubscription(userEntity.getNbSubscription());
    }


}
