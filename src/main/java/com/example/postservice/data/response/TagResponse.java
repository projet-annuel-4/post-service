package com.example.postservice.data.response;

public class TagResponse {

    private String name;
    public Long postId;

    public String getName() {
        return name;
    }

    public TagResponse setName(String name) {
        this.name = name;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public TagResponse setPostId(Long postId) {
        this.postId = postId;
        return this;
    }
}
