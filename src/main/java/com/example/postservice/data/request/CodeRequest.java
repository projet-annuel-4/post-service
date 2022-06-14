package com.example.postservice.data.request;

public class CodeRequest {

    private String language;
    private String content;
    private Boolean runnable;
    private Long post_id;


    public String getLanguage() {
        return language;
    }

    public CodeRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CodeRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getRunnable() {
        return runnable;
    }

    public CodeRequest setRunnable(Boolean runnable) {
        this.runnable = runnable;
        return this;
    }

    public Long getPost_id() {
        return post_id;
    }

    public CodeRequest setPost_id(Long post_id) {
        this.post_id = post_id;
        return this;
    }
}
