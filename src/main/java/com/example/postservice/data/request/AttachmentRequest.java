package com.example.postservice.data.request;

public class AttachmentRequest {

    private String url;
    private String description;
    private Long postId;

    public String getUrl() {
        return url;
    }

    public AttachmentRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttachmentRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public AttachmentRequest setPostId(Long postId) {
        this.postId = postId;
        return this;
    }
}
