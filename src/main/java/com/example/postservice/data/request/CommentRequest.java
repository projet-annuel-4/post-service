package com.example.postservice.data.request;

import com.sun.istack.NotNull;

public class CommentRequest {

    @NotNull
    private Long postId;
    @NotNull
    private Long answerId;
    @NotNull
    private Long userId;

    public Long getPostId() {
        return postId;
    }

    public CommentRequest setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public CommentRequest setAnswerId(Long answerId) {
        this.answerId = answerId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CommentRequest setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
