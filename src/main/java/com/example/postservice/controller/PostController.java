package com.example.postservice.controller;


import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.data.response.PostResponse;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.service.AttachmentService;
import com.example.postservice.service.PostService;
import com.example.postservice.service.TagService;
import com.example.postservice.service.UserService;
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

    public PostController(PostService postService, UserService userService, TagMapper tagMapper, TagService tagService, AttachmentMapper attachmentMapper, AttachmentService attachmentService) {
        this.postService = postService;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
        this.attachmentMapper = attachmentMapper;
        this.attachmentService = attachmentService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody PostRequest postRequest){
        //TODO :
        // - Vérifier que le content n'est pas vide (avec annotation spring)

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


    @GetMapping("/{postId}")
    public ResponseEntity<?> getById(@PathVariable @NotNull Long postId){
        var post = postService.getById(postId);

        return new ResponseEntity<>(toResponse(post), HttpStatus.FOUND);
    }


    /** Get by user **/

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable @NotNull Long userId){
        var user = userService.getById(userId);

        // TODO : Ne passe pas dans le if, ne renvoie pas d'erreur lorsque le user n'existe pas
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

    // get all by tag
    /** select * from post where id = (select post_id from tag where name = "js"); **/
    @GetMapping("{tagName}")
    public ResponseEntity<?> getAllByTag(@PathVariable String tagName){

        var tags = tagService.getAllTagByName(tagName);



        return new ResponseEntity<>(HttpStatus.FOUND);
    }




    // get les Posts des followers
    // liker un post
    // commenter un post
    //get les personne qui ont liké un post

    // Get les posts likés





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
