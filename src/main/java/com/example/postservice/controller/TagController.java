package com.example.postservice.controller;

import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.response.TagResponse;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    public TagController(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }



    @GetMapping("/name/{tagName}")
    public ResponseEntity<List<TagResponse>> getAllByName(@PathVariable String tagName){
        var tags = tagService.getAllTagByName(tagName)
                .stream()
                .map(this::entityToResponse)
                .collect(toList());

        return new ResponseEntity<>(tags, HttpStatus.FOUND);
    }



    private TagResponse entityToResponse(TagEntity tagEntity){
        return new TagResponse()
                .setName(tagEntity.getName())
                .setPostId(tagEntity.getId());
    }


}
