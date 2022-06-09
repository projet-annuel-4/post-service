package com.example.postservice.service;

import com.example.postservice.data.entities.LikeEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.TagEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.*;
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
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.tagRepository = tagRepository;
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

    public ArrayList<Optional<PostEntity>> getAllByTag(List<TagEntity> tagEntityList){

        var posts = new ArrayList<Optional<PostEntity>>();

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

    public ArrayList<Optional<UserEntity>> getUserLiked(List<LikeEntity> likeEntityList){

        var usersLiked = new ArrayList<Optional<UserEntity>>();

        likeEntityList.forEach(likeEntity -> {
            usersLiked.add(userRepository.findById(likeEntity.getUser().getId()));
        });

        return usersLiked;
    }

    public ArrayList<Optional<PostEntity>> getPostLiked(List<LikeEntity> likeEntityList){

        var postsLiked = new ArrayList<Optional<PostEntity>>();

        likeEntityList.forEach(likeEntity -> {
            postsLiked.add(postRepository.findById(likeEntity.getPost().getId()));
        });

        return postsLiked;
    }

    @Transactional
    public void delete(PostEntity post){
        attachmentRepository.deleteAllByPostId(post.getId());
        tagRepository.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());

        //TODO : supprimer les commentaires sans supprimer les posts associés

        post.setUser(null);
        postRepository.deleteById(post.getId());
    }


}
