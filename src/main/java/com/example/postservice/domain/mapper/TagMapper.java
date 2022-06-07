package com.example.postservice.domain.mapper;

import com.example.postservice.data.request.TagRequest;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {


    public TagRequest toRequest(String tagName, Long postId){
        return new TagRequest()
                .setName(tagName)
                .setPostId(postId);
    }
}
