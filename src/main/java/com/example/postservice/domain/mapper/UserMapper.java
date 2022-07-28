package com.example.postservice.domain.mapper;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.request.UserRequest;
import com.example.postservice.data.response.UserResponse;
import com.example.postservice.domain.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity eventToEntity(UserEvent userEvent){
        return new UserEntity()
                .setId(userEvent.getId())
                .setFirstname(userEvent.getFirstName())
                .setLastname(userEvent.getLastName())
                .setEmail(userEvent.getEmail());
    }

    public static UserModel entityToModel(UserEntity userEntity){
        return new UserModel()
                .setId(userEntity.getId())
                .setFirstname(userEntity.getFirstname())
                .setLastname(userEntity.getLastname())
                .setEmail(userEntity.getEmail())
                .setNbFollowers(userEntity.getNbFollowers())
                .setNbSubscriptions(userEntity.getNbSubscription());
    }

    public UserRequest entityToRequest(UserEntity userEntity){
        return new UserRequest()
                .setFirstname(userEntity.getFirstname())
                .setLastname(userEntity.getLastname())
                .setEmail(userEntity.getEmail())
                .setNbFollowers(userEntity.getNbFollowers())
                .setNbSubscription(userEntity.getNbSubscription());
    }

    public static UserResponse modelToResponse(UserModel userModel){
        return new UserResponse()
                .setId(userModel.getId())
                .setFirstname(userModel.getFirstname())
                .setLastname(userModel.getLastname())
                .setEmail(userModel.getEmail())
                .setNbFollowers(userModel.getNbFollowers())
                .setNbSubscription(userModel.getNbSubscriptions());
    }


}
