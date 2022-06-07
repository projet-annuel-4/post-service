package com.example.postservice.data.request;

public class TagRequest {

    private String name;
    private Long postId;

    public String getName() {
        return name;
    }

    public TagRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return postId;
    }

    public TagRequest setPostId(Long postId) {
        this.postId = postId;
        return this;
    }
}
