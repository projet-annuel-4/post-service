package com.example.postservice.domain.model;

import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.response.TagResponse;

import java.time.LocalDateTime;
import java.util.List;

public class PostModel {

    private Long id;
    private String content;
    private Integer nbLike;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private List<TagResponse> tags;
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public PostModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostModel setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getNbLike() {
        return nbLike;
    }

    public PostModel setNbLike(Integer nbLike) {
        this.nbLike = nbLike;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public PostModel setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public PostModel setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public List<TagResponse> getTags() {
        return tags;
    }

    public PostModel setTags(List<TagResponse> tags) {
        this.tags = tags;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public PostModel setUser(UserEntity user) {
        this.user = user;
        return this;
    }

}
