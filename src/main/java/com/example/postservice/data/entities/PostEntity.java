package com.example.postservice.data.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer nbLike;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Temporal(TemporalType.DATE)
    private Date updateDate;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public PostEntity setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public PostEntity setUpdateDate(Date updateDate) {
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
