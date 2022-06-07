package com.example.postservice.data.response;

import com.example.postservice.data.entities.AttachmentEntity;
import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public class PostResponse {

    private String content;
    private Integer nbLike;
    private Date creationDate;
    private Date updateDate;
    private UserEntity user;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public PostResponse setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public PostResponse setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
