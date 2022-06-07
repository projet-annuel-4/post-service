package com.example.postservice.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;


    public Long getId() {
        return id;
    }

    public TagEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagEntity setName(String name) {
        this.name = name;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public TagEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }
}
