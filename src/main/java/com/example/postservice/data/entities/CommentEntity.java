package com.example.postservice.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PostEntity post;

    @ManyToOne
    private PostEntity answer;

    @ManyToOne
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public CommentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public CommentEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }

    public PostEntity getAnswer() {
        return answer;
    }

    public CommentEntity setAnswer(PostEntity answer) {
        this.answer = answer;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public CommentEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
