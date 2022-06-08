package com.example.postservice.service;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.repository.TagRepository;
import com.example.postservice.data.request.TagRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public TagEntity update(TagRequest tagRequest){
        var tag = tagRepository.findTagEntitiesByNameAndPostId(tagRequest.getName(),
                                                                        tagRequest.getPostId())
                .setName(tagRequest.getName());
        return tagRepository.save(tag);
    }

    public Optional<TagEntity> getById(Long tagId){
        return tagRepository.findById(tagId);
    }

    public TagEntity getByName(String tagName){
        return tagRepository.findByName(tagName);
    }

    public TagEntity getByNameAndPostId(String tagName, Long postId){
        return tagRepository.findTagEntitiesByNameAndPostId(tagName,postId);
    }

    public Optional<List<TagEntity>> getAllTagByName(String tagName){
        return tagRepository.findTagEntitiesByName(tagName);
    }
}
