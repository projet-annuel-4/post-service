package com.example.postservice.domain.mapper;

import com.example.postservice.data.UserEvent;
import com.example.postservice.data.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity eventToEntity(UserEvent userEvent){
        return new UserEntity()
                .setFirstname(userEvent.getFirstName())
                .setLastname(userEvent.getLastName())
                .setEmail(userEvent.getEmail());
    }
}
