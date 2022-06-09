package com.example.postservice.service;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.LikeRepository;
import com.example.postservice.data.repository.PostRepository;
import com.example.postservice.data.request.PostRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
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

    public Optional<PostEntity> getById(Long postId){
        return postRepository.findById(postId);
    }

    public List<PostEntity> getAll(){
        return postRepository.findAll();
    }
    public List<PostEntity> getAllByUser(UserEntity user){
        return postRepository.getAllByUser(user);
    }

    public ArrayList<Object> getAllByTag(List<TagEntity> tagEntityList){

        var posts = new ArrayList<>();

        tagEntityList.forEach(tagEntity -> {
            posts.add(postRepository.findById(tagEntity.getPost().getId()));
        });

        return posts;
    }

    public PostEntity update(Long postId, PostRequest postRequest){

        var post = postRepository.getById(postId)
                .setContent(postRequest.getContent())
                .setNbLike(likeRepository.countLikeEntitiesByPostId(postId))
                .setUpdateDate(new Date());
        return postRepository.save(post);
    }

    //TODO : A tester
    public ArrayList<Object> getUserLiked(List<LikeEntity> likeEntityList){

        var usersLiked = new ArrayList<>();

        likeEntityList.forEach(likeEntity -> {
            usersLiked.add(postRepository.findById(likeEntity.getUser().getId()));
        });

        return usersLiked;
    }

    //TODO : A tester
    public ArrayList<Object> getPostLiked(List<PostEntity> postEntityList){

        var postsLiked = new ArrayList<>();

        postEntityList.forEach(postEntity -> {
            postsLiked.add(postRepository.findById(postEntity.getUser().getId()));
        });

        return postsLiked;
    }

    @Transactional
    public void delete(Long postId){
        postRepository.deleteById(postId);
    }


}
