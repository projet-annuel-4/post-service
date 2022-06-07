package com.example.postservice.domain.mapper;

import com.example.postservice.data.request.AttachmentRequest;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    public AttachmentRequest toRequest(String url, String description, Long postId){
        return new AttachmentRequest()
                .setUrl(url)
                .setDescription(description)
                .setPostId(postId);
    }
}
