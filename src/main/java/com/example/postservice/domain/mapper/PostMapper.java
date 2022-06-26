package com.example.postservice.domain.mapper;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.data.response.PostResponse;
import com.example.postservice.domain.model.PostModel;
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

    public static PostModel entityToModel(PostEntity postEntity){
        return new PostModel()
                .setId(postEntity.getId())
                .setContent(postEntity.getContent())
                .setNbLike(postEntity.getNbLike())
                .setCreationDate(postEntity.getCreationDate())
                .setUpdateDate(postEntity.getUpdateDate())
                .setUser(postEntity.getUser());
    }

    public PostEntity modelToEntity(PostModel postModel){
        return new PostEntity()
                .setId(postModel.getId())
                .setContent(postModel.getContent())
                .setNbLike(postModel.getNbLike())
                .setCreationDate(postModel.getCreationDate())
                .setUpdateDate(postModel.getUpdateDate())
                .setUser(postModel.getUser());
    }

    public static PostResponse modelToResponse(PostModel postModel){
        return new PostResponse()
                .setId(postModel.getId())
                .setContent(postModel.getContent())
                .setNbLike(postModel.getNbLike())
                .setCreationDate(postModel.getCreationDate())
                .setUpdateDate(postModel.getUpdateDate())
                .setTags(postModel.getTags())
                .setUser(postModel.getUser());
    }
}
