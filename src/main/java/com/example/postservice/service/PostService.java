package com.example.postservice.service;

import com.example.postservice.data.entities.*;
import com.example.postservice.data.repository.*;
import com.example.postservice.data.request.PostFilterRequest;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.PostMapper;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.domain.model.PostModel;
import com.example.postservice.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentService attachmentService;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final TagMapper tagMapper;
    private final CommentRepository commentRepository;
    private final FollowerService followerService;
    private final CodeService codeService;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, AttachmentService attachmentService, TagRepository tagRepository, TagService tagService, TagMapper tagMapper, CommentRepository commentRepository, FollowerService followerService, CodeService codeService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.attachmentService = attachmentService;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.commentRepository = commentRepository;
        this.followerService = followerService;
        this.codeService = codeService;
    }


    public PostModel create(PostRequest postRequest, UserEntity user){

        //Premier enregistrement pour avoir l'id du post
        var post = new PostEntity()
                .setTitle(postRequest.getTitle())
                .setContent(postRequest.getContent())
                .setNbLike(0)
                .setCreationDate(LocalDateTime.now())
                .setUpdateDate(null)
                .setUser(user);
        postRepository.save(post);

        if(!postRequest.getTagName().isBlank()) {
            var tagRequest = tagMapper.toRequest(postRequest.getTagName(), post.getId());
            tagService.create(tagRequest, post);
        }

        if(!postRequest.getAttachmentUrl().isBlank() && !postRequest.getAttachmentDescription().isBlank()){
            var attachmentRequest = attachmentMapper.toRequest(
                    postRequest.getAttachmentUrl(),
                    postRequest.getAttachmentDescription(),
                    postRequest.getUserId()
            );
            attachmentService.create(attachmentRequest, post);
        }

        String content = postRequest.getContent();
        if(!postRequest.getContent().isBlank()) {
            var codeMap = codeService.create(postRequest.getContent(), post);
/*
            AtomicReference<String> updateContent = new AtomicReference<>("");
            codeMap.forEach((language, code) -> {
                updateContent.set(updateContent + code.getCode());
            });

            if(!updateContent.get().isBlank()) {
                content = updateContent.get();
            }

 */
        }

        var postToUpdate = postRepository.findById(post.getId());
        postToUpdate.get()
                .setContent(content)
                .setNbLike(0)
                .setCreationDate(LocalDateTime.now())
                .setUpdateDate(null)
                .setUser(user);
        postRepository.save(post);

        return PostMapper.entityToModel(postToUpdate.get());
    }

    public PostModel update(Long postId, PostRequest postRequest){
        var post = postRepository.getById(postId)
                .setTitle(postRequest.getTitle())
                .setContent(postRequest.getContent())
                .setNbLike(likeRepository.countLikeEntitiesByPostId(postId))
                .setUpdateDate(LocalDateTime.now());

        postRepository.save(post);

        var postTags = tagRepository.findTagEntitiesByPostId(postId).get()
                .stream()
                .map(TagMapper::toResponse)
                .collect(toList());

        var postModel = PostMapper.entityToModel(post);
        postModel.setTags(postTags);


        return postModel;
    }

    public PostModel getById(Long postId){
        var post =  postRepository.findById(postId);

        var postTags = tagRepository.findTagEntitiesByPostId(postId).get()
                .stream()
                .map(TagMapper::toResponse)
                .collect(toList());

        var postModel = PostMapper.entityToModel(post.get());
        postModel.setTags(postTags);

        return postModel;
    }

    public List<PostModel> getAll(){
        var posts =  postRepository.findAll()
                .stream()
                .map(PostMapper::entityToModel)
                .collect(toList());

        posts = addTagsToPostModelList(posts);

        return posts;
    }

    public List<PostModel> getAllByUser(UserEntity user){
        var posts =  postRepository.getAllByUser(user)
                .stream()
                .map(PostMapper::entityToModel)
                .collect(toList());

        posts = addTagsToPostModelList(posts);

        return posts;
    }

    public List<PostModel> getAllByTitle(String title){
        return postRepository.getAllByTitle(title)
                .stream()
                .map(PostMapper::entityToModel)
                .collect(toList());
    }

    public ArrayList<PostModel> getAllByTag(List<TagEntity> tagEntityList){

        var posts = new ArrayList<PostModel>();

        tagEntityList.forEach(tagEntity -> {
            posts.add(this.getById(tagEntity.getPost().getId()));
        });

        return posts;
    }
    public List<PostModel> getAllByTags(List<TagEntity> tagEntityList){
        var posts = new ArrayList<PostModel>();
        tagEntityList.forEach(tagEntity -> {
            posts.add(this.getById(tagEntity.getPost().getId()));
        });
        return posts;
    }

    public List<PostModel> getAllWithFilter(PostFilterRequest filters){

        var postsWithFilter = new ArrayList<PostModel>();

        var dateForQuery = LocalDateTime.now();

        if (!filters.getCreationDate().isEmpty() && filters.getCreationDate() != null) {
            if(DateTimeUtil.isValid(filters.getCreationDate())){
                dateForQuery = DateTimeUtil.dateFromString(filters.getCreationDate());
            }
        } else {
            dateForQuery = DateTimeUtil.dateFromString("1900-01-01 00:00:00");
        }


        if(!filters.getTitle().isEmpty() && filters.getTitle() != null){
            postsWithFilter.addAll(this.getAllByTitle(filters.getTitle()));
        }


        if(!filters.getTagName().isEmpty()){
            var tags = tagRepository.findTagEntitiesByName(filters.getTagName());
            if(tags.isPresent()){
                var postsByTag = this.getAllByTag(tags.get());
                postsByTag.forEach(postEntity -> {
                    if(postEntity != null) {
                        postsWithFilter.add(postEntity);
                    }
                });
            }
        }

        var postFound = postRepository.findAllByContentOrUpdateDate(filters.getContent(), dateForQuery)
                .stream()
                .map(PostMapper::entityToModel)
                .collect(toList());

        postsWithFilter.addAll(postFound);


        postsWithFilter.forEach(postModel -> {
            var postTags = tagRepository.findTagEntitiesByPostId(postModel.getId()).get()
                    .stream()
                    .map(TagMapper::toResponse)
                    .collect(toList());

            postModel.setTags(postTags);
        });

        return postsWithFilter;
    }

    public ArrayList<Optional<UserEntity>> getUserLiked(List<LikeEntity> likeEntityList){
        var usersLiked = new ArrayList<Optional<UserEntity>>();

        likeEntityList.forEach(likeEntity -> {
            usersLiked.add(userRepository.findById(likeEntity.getUser().getId()));
        });

        return usersLiked;
    }

    public ArrayList<PostModel> getPostLiked(List<LikeEntity> likeEntityList){
        var postsLiked = new ArrayList<PostModel>();

        likeEntityList.forEach(likeEntity -> {
            postsLiked.add(this.getById(likeEntity.getPost().getId()));
        });

        return postsLiked;
    }

    public ArrayList<PostModel> getAllPostAnswersById(Long postId){
        var comments = commentRepository.findAllByPostId(postId);

        var answers = new ArrayList<PostModel>();
        comments.forEach(commentEntity -> {
            answers.add(this.getById(commentEntity.getAnswer().getId()));
        });

        return answers;
    }

    public List<PostModel> getAllSubscriptionPostByIdUser(Long userId){
        var subscriptionsLink = followerService.getSubscriptionsByUserId(userId);

        var posts = new ArrayList<PostModel>();
        subscriptionsLink.forEach(subscription -> posts.addAll(this.getAllByUser(subscription.getUser())));

        return posts;
    }

    @Transactional
    public void delete(PostEntity post){
        attachmentRepository.deleteAllByPostId(post.getId());
        tagRepository.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());
        commentRepository.deleteAllByPostId(post.getId());
        codeService.deleteAllByPostId(post.getId());

        post.setUser(null);
        postRepository.deleteById(post.getId());
    }




    private List<PostModel> addTagsToPostModelList(List<PostModel> postModelList){
        var posts = new ArrayList<PostModel>();
        postModelList.forEach(postModel -> {
            var postTags = tagRepository.findTagEntitiesByPostId(postModel.getId()).get()
                    .stream()
                    .map(TagMapper::toResponse)
                    .collect(toList());

            postModel.setTags(postTags);
            posts.add(postModel);
        });

        return posts;
    }


}
