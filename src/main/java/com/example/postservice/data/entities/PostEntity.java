package com.example.postservice.data.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Integer nbLike;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public Long getId() {
        return id;
    }

    public PostEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getNbLike() {
        return nbLike;
    }

    public PostEntity setNbLike(Integer nbLike) {
        this.nbLike = nbLike;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public PostEntity setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public PostEntity setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public PostEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

}
