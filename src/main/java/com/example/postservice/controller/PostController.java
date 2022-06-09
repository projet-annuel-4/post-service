package com.example.postservice.controller;


import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.request.CommentRequest;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.data.response.PostResponse;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.CommentMapper;
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
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public PostController(PostService postService, UserService userService, TagMapper tagMapper, TagService tagService, AttachmentMapper attachmentMapper, AttachmentService attachmentService, LikeService likeService, CommentService commentService, CommentMapper commentMapper) {
        this.postService = postService;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
        this.attachmentMapper = attachmentMapper;
        this.attachmentService = attachmentService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
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
        //TODO : Mettre à jour le format des dates dans les entities

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

    /**
     * @apiNote SELECT * FROM user  WHERE id = ( SELECT user_id FROM user_like WHERE post_id = id du post )
     * @param postId : Id of the user
     * @return Users who liked the post
     */
    @GetMapping("/{postId}/userLiked")
    public ResponseEntity<?> getUsersLiked(@PathVariable Long postId){
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

    /**
     * @apiNote SELECT * FROM post WHERE id = ( SELECT post_id FROM user_like WHERE user_id = id du user )
     * @param userId : Id of the user
     * @return Posts that the user liked
     */
    @GetMapping("/userId/{userId}/postLiked")
    public ResponseEntity<?> getPostLiked(@PathVariable Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var likeList = likeService.getAllByUserId(userId);
        if(likeList.isPresent() && likeList.get().isEmpty()){
            return new ResponseEntity<>("The user didn't like any post", HttpStatus.NO_CONTENT);
        }
        
        var postLiked = postService.getPostLiked(likeList.get());

        return new ResponseEntity<>(postLiked, HttpStatus.FOUND);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<?> delete (@PathVariable Long postId){
        var post = postService.getById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
        postService.delete(post.get());

        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }




    // commenter un post | les commentaire sont des posts (quand même créer une entity comment)

    /**
     * @apiNote Côté front, appeler la route de création de post PUIS la route comment
     *          Pour commenter un commentaires -> inverser les id dans le commentRequest
     * @param commentRequest : Object with Id of the post question and Id of the answer post
     */
    @PostMapping("/answer")
    public ResponseEntity<?> comment(@RequestBody @NotNull CommentRequest commentRequest){
        var user = userService.getById(commentRequest.getUserId());
        if(user.isEmpty()) return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);

        var post = postService.getById(commentRequest.getPostId());
        if(post.isEmpty())  return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        var answer = postService.getById(commentRequest.getAnswerId());
        if(answer.isEmpty()) return new ResponseEntity<>("Answer post not found", HttpStatus.NOT_FOUND);


        var comment = commentService.create(post.get(), answer.get(), user.get());

        return new ResponseEntity<>(commentMapper.entityToResponse(comment), HttpStatus.ACCEPTED);
    }



    //Get toutes les réponses d'un post
    /** SELECT * FROM post WHERE post_id = (SELECT answerId FROM comment WHERE post_id = id du post) */


    // Get toutes les réponses d'un user
    /**           SELECT * FROM post WHERE postId = (SELECT answerId FROM comment)
     *            les posts qui sont des réponses    les id des posts qui sont des réponses
     *
     *
     *
     *      select a.post_id
     *      from post a, comment b
     *      where a.user_id = b.user_id;
     *
     *
     * */

    // get les Posts des followers





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
