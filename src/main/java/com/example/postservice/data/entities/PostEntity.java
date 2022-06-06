package com.example.postservice.data.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer nbLike;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Set<TagEntity> tag;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    private Set<AttachmentEntity> attachment;


    public Long getId() {
        return id;
    }

    public PostEntity setId(Long id) {
        this.id = id;
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
