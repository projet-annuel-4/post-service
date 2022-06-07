package com.example.postservice.data.entities;


import javax.persistence.*;

@Entity
@Table(name = "attachment")
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String description;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;


    public Long getId() {
        return id;
    }

    public AttachmentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AttachmentEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttachmentEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public AttachmentEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }
}
