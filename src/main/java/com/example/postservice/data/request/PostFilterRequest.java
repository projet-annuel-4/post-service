package com.example.postservice.data.request;


public class PostFilterRequest {

    private String title;
    private String content;
    private String tagName;

    private String creationDate;


    public String getTitle() {
        return title;
    }

    public PostFilterRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostFilterRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTagName() {
        return tagName;
    }

    public PostFilterRequest setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public PostFilterRequest setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }
}
