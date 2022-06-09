package com.example.postservice.data.response;

public class CommentResponse {

    private Long postId;
    private Long answerId;
    private Long userId;

    public Long getPostId() {
        return postId;
    }

    public CommentResponse setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public CommentResponse setAnswerId(Long answerId) {
        this.answerId = answerId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CommentResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
