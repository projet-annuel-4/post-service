package com.example.postservice.data.request;


import java.time.LocalDateTime;
import java.util.List;

public class PostRequest {

    private String title;
    private String content;
    private Integer nbLike;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Long userId;
    private List<String> tagsName;
    private String attachmentUrl;
    private String attachmentDescription;

    public String getTitle() {
        return title;
    }

    public PostRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getNbLike() {
        return nbLike;
    }

    public PostRequest setNbLike(Integer nbLike) {
        this.nbLike = nbLike;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public PostRequest setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public PostRequest setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public PostRequest setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public List<String> getTagsName() {
        return tagsName;
    }

    public PostRequest setTagsName(List<String> tagsName) {
        this.tagsName = tagsName;
        return this;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public PostRequest setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
        return this;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public PostRequest setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
        return this;
    }
}
