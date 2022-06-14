package com.example.postservice.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "code")
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String language;
    private String content;
    private Boolean runnable; /* pour savor si le code est executable ou pas */
    @ManyToOne()
    private PostEntity post;

    
    public Long getId() {
        return id;
    }

    public CodeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public CodeEntity setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CodeEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getRunnable() {
        return runnable;
    }

    public CodeEntity setRunnable(Boolean runnable) {
        this.runnable = runnable;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public CodeEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }
}
