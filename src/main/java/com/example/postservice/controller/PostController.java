package com.example.postservice.controller;


import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.data.response.PostResponse;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.service.*;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final TagMapper tagMapper;
    private final TagService tagService;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentService attachmentService;
    private final LikeService likeService;

    public PostController(PostService postService, UserService userService, TagMapper tagMapper, TagService tagService, AttachmentMapper attachmentMapper, AttachmentService attachmentService, LikeService likeService) {
        this.postService = postService;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
        this.attachmentMapper = attachmentMapper;
        this.attachmentService = attachmentService;
        this.likeService = likeService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody PostRequest postRequest){
        //TODO :
        // - Possible d'avoir un post sans content (donc juste une image) ??
        // -    Sinon : Vérifier que le content n'est pas vide (avec annotation spring)

        var user = userService.getById(postRequest.getUserId());
        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var post = postService.create(postRequest, user.get());

        if(!Objects.equals(postRequest.getTagName(), "")) {
            var tagRequest = tagMapper.toRequest(postRequest.getTagName(), post.getId());
            tagService.create(tagRequest, post);
        }

        if(!Objects.equals(postRequest.getAttachmentUrl(), "") && !Objects.equals(postRequest.getAttachmentDescription(), "")){
            var attachmentRequest = attachmentMapper.toRequest(
                    postRequest.getAttachmentUrl(),
                    postRequest.getAttachmentDescription(),
                    postRequest.getUserId()
            );
            attachmentService.create(attachmentRequest, post);
        }

        return new ResponseEntity<>(toResponse(post), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@PathVariable Long postId, @RequestBody PostRequest postRequest){
        //TODO : Mettre à jour le fromat des dates dans les entities

        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(" Post not found", HttpStatus.NOT_FOUND);
        }

        if(!Objects.equals(postRequest.getTagName(), "")) {
            var tagRequest = tagMapper.toRequest(postRequest.getTagName(), post.get().getId());

            var tagExist = tagService.getByNameAndPostId(postRequest.getTagName(), postId);
            if(tagExist != null) {
                tagService.update(tagRequest);
            } else {
                tagService.create(tagRequest, post.get());
            }
        }

        if(!Objects.equals(postRequest.getAttachmentUrl(), "") && !Objects.equals(postRequest.getAttachmentDescription(), "")){
            var attachmentRequest = attachmentMapper.toRequest(
                    postRequest.getAttachmentUrl(),
                    postRequest.getAttachmentDescription(),
                    postRequest.getUserId());

            var attachmentExist = attachmentService.getByUrlAndPostId(postRequest.getAttachmentUrl(), postId);
            if(attachmentExist != null) {
                attachmentService.update(attachmentRequest);
            } else {
                attachmentService.create(attachmentRequest, post.get());
            }
        }

        postService.update(postId, postRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getById(@PathVariable @NotNull Long postId){
        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(" Post not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toResponse(post.get()), HttpStatus.FOUND);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable @NotNull Long userId){
        var user = userService.getById(userId);

        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

         var posts = postService.getAllByUser(user.get())
                .stream()
                .map(this::toResponse)
                .collect(toList());

        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        var posts = postService.getAll()
                .stream()
                .map(this::toResponse)
                .collect(toList());

        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    /** SELECT * FROM post WHERE id = (SELECT post_id FROM tag WHERE name = "js"); **/
    @GetMapping("/tagName/{tagName}")
    public ResponseEntity<?> getAllByTag(@PathVariable String tagName){
        var tags = tagService.getAllTagByName(tagName);
        if(tags.isEmpty()){
            return new ResponseEntity<>(" Tag not found", HttpStatus.NOT_FOUND);
        }

        var posts = postService.getAllByTag(tags.get());
        if(posts.isEmpty()){
            return new ResponseEntity<>("No post with this tag", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }


    /**
     *
     * @param postId : Id of the post
     * @param userId : Id of the user who likes
     */
    @PostMapping("/{postId}/like/userId/{userId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(" Post not found", HttpStatus.NOT_FOUND);
        }

        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var alreadyLiked = likeService.getByPostAndUser(post.get(), user.get());
        if(alreadyLiked != null){
            return new ResponseEntity<>("You have already like this post", HttpStatus.FORBIDDEN);
        }

        likeService.like(post.get(), user.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     *
     * @param postId :Id of the post
     * @param userId : Id of the user who likes
     */
    @PostMapping("/{postId}/dislike/userId/{userId}")
    public ResponseEntity<?> dislike(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(" Post not found", HttpStatus.NOT_FOUND);
        }

        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        likeService.dislike(post.get(), user.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //Get les personnes qui ont likés un post    
    /**
     * @param postId
     * @apiNote SELECT * FROM user  WHERE id = ( SELECT user_id FROM user_like WHERE post_id = id du post )
     * @return Users who liked the post
     */
    
     //TODO : A tester
    @GetMapping("/{postId}/userLiked/")
    public ResponseEntity<?> usersliked(@PathVariable Long postId){
        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(" Post not found", HttpStatus.NOT_FOUND);
        }

        var postLikeList = likeService.getAllByPostId(postId);
        if(postLikeList.isEmpty()){
            return new ResponseEntity<>("The post has no likes", HttpStatus.NO_CONTENT);
        }

        var userLiked = postService.getUserLiked(postLikeList.get());
        return new ResponseEntity<>(userLiked, HttpStatus.FOUND);
    }


    // Get les posts que le user a liké
    /**
     * @param userId
     * @apiNote SELECT * FROM post WHERE id = ( SELECT post_id FROM user_like WHERE user_id = id du user )
     * @return Posts that the user liked
     */
    
    //TODO : A tester
    @GetMapping("/{userId}/postLiked/")
    public ResponseEntity<?> postLiked(@PathVariable Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var postLikeList = likeService.getAllByUserId(userId);
        if(postLikeList.isEmpty()){
            return new ResponseEntity<>("The user didn't like any post", HttpStatus.NO_CONTENT);
        }
        
        var postLiked = postService.getPostLiked(postLikeList.get());

        return new ResponseEntity<>(postLiked, HttpStatus.FOUND);
    }






    // get les Posts des followers
    // commenter un post | les commentaire sont des posts (quand même créer une entity comment)
    
    //Supprimer un post


    //TODO : Mettre dans une classe PostMapper
    private PostResponse toResponse(PostEntity postEntity){
        return new PostResponse()
                .setContent(postEntity.getContent())
                .setNbLike(postEntity.getNbLike())
                .setCreationDate(postEntity.getCreationDate())
                .setUpdateDate(postEntity.getCreationDate())
                .setUser(postEntity.getUser());
    }




}
