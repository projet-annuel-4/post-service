package com.example.postservice.data.response;

import com.example.postservice.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Integer nbLike;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private List<TagResponse> tags;
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public PostResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostResponse setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getNbLike() {
        return nbLike;
    }

    public PostResponse setNbLike(Integer nbLike) {
        this.nbLike = nbLike;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public PostResponse setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public PostResponse setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public List<TagResponse> getTags() {
        return tags;
    }

    public PostResponse setTags(List<TagResponse> tags) {
        this.tags = tags;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public PostResponse setUser(UserEntity user) {
        this.user = user;
        return this;
    }

}
