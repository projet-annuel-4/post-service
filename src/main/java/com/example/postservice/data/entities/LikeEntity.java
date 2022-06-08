package com.example.postservice.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_like")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PostEntity post;

    @ManyToOne
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public LikeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public LikeEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public LikeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
