package com.example.postservice.service;

import com.example.postservice.data.entities.*;
import com.example.postservice.data.repository.*;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.domain.Extractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final FollowerService followerService;
    private final CodeService codeService;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, TagRepository tagRepository, CommentRepository commentRepository, FollowerService followerService, CodeService codeService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
        this.followerService = followerService;
        this.codeService = codeService;
    }


    public PostEntity create(PostRequest postRequest, UserEntity user){

        //Premier enregistrement pour avoir l'id du post
        var post = new PostEntity()
                .setContent(postRequest.getContent())
                .setNbLike(0)
                .setCreationDate(new Date())
                .setUpdateDate(null)
                .setUser(user);
        postRepository.save(post);

        String content = postRequest.getContent();

        //TODO : utiliser isBlank() à la place de equals
        if(!Objects.equals(postRequest.getContent(), "")) {
            var codeMap = codeService.create(postRequest.getContent(), post);

            AtomicReference<String> updateContent = new AtomicReference<>("");
            codeMap.forEach((language, code) -> {
                updateContent.set(updateContent + code.getCode());
            });

            content = updateContent.get();
        }

        var postToUpdate = postRepository.findById(post.getId());
        postToUpdate.get()
                .setContent(content)
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

    public ArrayList<Optional<PostEntity>> getAllPostAnswersById(Long postId){
        var comments = commentRepository.findAllByPostId(postId);

        var answers = new ArrayList<Optional<PostEntity>>();
        comments.forEach(commentEntity -> {
            answers.add(postRepository.findById(commentEntity.getAnswer().getId()));
        });

        return answers;
    }

    public List<PostEntity> getAllSubscriptionPostByIdUser(Long userId){
        var subscriptionsLink = followerService.getSubscriptionsByUserId(userId);

        var posts = new ArrayList<PostEntity>();
        subscriptionsLink.forEach(subscription -> posts.addAll(postRepository.getAllByUser(subscription.getUser())));

        return posts;
    }

    @Transactional
    public void delete(PostEntity post){
        attachmentRepository.deleteAllByPostId(post.getId());
        tagRepository.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());
        commentRepository.deleteAllByPostId(post.getId());

        post.setUser(null);
        postRepository.deleteById(post.getId());
    }


}
