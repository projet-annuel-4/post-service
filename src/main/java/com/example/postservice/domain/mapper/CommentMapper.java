package com.example.postservice.domain.mapper;

import com.example.postservice.data.entities.CommentEntity;
import com.example.postservice.data.response.CommentResponse;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse entityToResponse(CommentEntity commentEntity){
        return new CommentResponse()
                .setPostId(commentEntity.getPost().getId())
                .setAnswerId(commentEntity.getPost().getId())
                .setUserId(commentEntity.getUser().getId());
    }
}
