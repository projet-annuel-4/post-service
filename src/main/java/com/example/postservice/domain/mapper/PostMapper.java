package com.example.postservice.domain.mapper;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.request.PostRequest;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostRequest entityToRequest(PostEntity postEntity){
        return new PostRequest()
                .setContent(postEntity.getContent())
                .setNbLike(postEntity.getNbLike())
                .setCreationDate(postEntity.getCreationDate())
                .setUpdateDate(postEntity.getUpdateDate())
                .setUserId(postEntity.getUser().getId());
    }
}
