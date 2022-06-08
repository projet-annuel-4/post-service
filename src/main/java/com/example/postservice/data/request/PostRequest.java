package com.example.postservice.data.request;


import java.util.Date;

public class PostRequest {

    private String content;
    private Integer nbLike;
    private Date creationDate;
    private Date updateDate;
    private Long userId;
    private String tagName;
    private String attachmentUrl;
    private String attachmentDescription;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public PostRequest setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public PostRequest setUpdateDate(Date updateDate) {
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

    public String getTagName() {
        return tagName;
    }

    public PostRequest setTagName(String tagName) {
        this.tagName = tagName;
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
