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









}
