package com.example.postservice.service;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.PostRepository;
import com.example.postservice.data.request.PostRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public PostEntity create(PostRequest postRequest, UserEntity user){
        var post = new PostEntity()
                .setContent(postRequest.getContent())
                .setNbLike(0)
                .setCreationDate(new Date())
                .setUpdateDate(null)
                .setUser(user);
        postRepository.save(post);
        return post;
    }

    public PostEntity getById(Long postId){
        return postRepository.getById(postId);
    }

    public List<PostEntity> getAllByUser(UserEntity user){
        return postRepository.getAllByUser(user);
    }

    public List<PostEntity> getAll(){
        return postRepository.findAll();
    }


    public PostEntity update(Long postId, PostRequest postRequest, UserEntity user){

        var post = postRepository.getById(postId)
                .setContent(postRequest.getContent())
                .setNbLike(postRequest.getNbLike())
                .setUser(user);
        return postRepository.save(post);
    }

    public PostEntity like(Long postId, Long user_like){

        var post = postRepository.getById(postId);

        var postLiked = new PostEntity()
                .setContent(post.getContent())
                .setNbLike(post.getNbLike() + 1)
                .setUser(post.getUser());

        return postRepository.save(postLiked);
    }


    public void delete(Long postId){
        postRepository.deleteById(postId);
    }


}
