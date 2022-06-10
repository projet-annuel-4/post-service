package com.example.postservice.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "follower")
public class FollowersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private UserEntity follower;


    public Long getId() {
        return id;
    }
    public FollowersEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public FollowersEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public FollowersEntity setFollower(UserEntity followers) {
        this.follower = followers;
        return this;
    }
}
