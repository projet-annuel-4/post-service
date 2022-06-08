package com.example.postservice.service;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.repository.TagRepository;
import com.example.postservice.data.request.TagRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagEntity create(TagRequest tagRequest, PostEntity postEntity){
        var tag = new TagEntity()
                .setName(tagRequest.getName())
                .setPost(postEntity);

        return tagRepository.save(tag);
    }

    public List<TagEntity> getAllTagByName(String tagName){
        return tagRepository.getTagEntitiesByName(tagName);
    }
}
