package com.example.postservice.controller;


import com.example.postservice.data.request.CommentRequest;
import com.example.postservice.data.request.PostFilterRequest;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.data.response.PostResponse;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.CommentMapper;
import com.example.postservice.domain.mapper.PostMapper;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.service.*;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String POST_NOT_FOUND = "Post not found";
    

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final TagMapper tagMapper;
    private final TagService tagService;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentService attachmentService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public PostController(PostService postService, PostMapper postMapper, UserService userService, TagMapper tagMapper, TagService tagService, AttachmentMapper attachmentMapper, AttachmentService attachmentService, LikeService likeService, CommentService commentService, CommentMapper commentMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
        this.attachmentMapper = attachmentMapper;
        this.attachmentService = attachmentService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    /**
     * Gestion du code dans les posts :
         * Avant d'enregister le post
         *     Dans le content : recuperer le code entre les balises -> https://waytolearnx.com/2020/05/extraire-une-chaine-entre-deux-balises-en-java.html
         *             regex
         *         - créer l'entitiy Code
         *             codeRepostitory.create(language, code, runnable)
         *         - Recupérer l'id de l'object Code créé
         *
         *     Dans le content remplacer le texte du code entre les balises par l'id de l'entityCode
         * Enregistrer le post
     *
     * @param postRequest : PostRequest(
     *                                  String content;
     *                                  Integer nbLike;
     *                                  LocalDateTime creationDate;
     *                                  LocalDateTime updateDate;
     *                                  Long userId;
     *                                  String tagName;
     *                                  String attachmentUrl;
     *                                  String attachmentDescription;
     * @return : The post created
     */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody PostRequest postRequest){
        var user = userService.getById(postRequest.getUserId());
        if(user.isEmpty()){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var post = postService.create(postRequest, user.get());

        return new ResponseEntity<>(PostMapper.modelToResponse(post), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@PathVariable Long postId, @RequestBody PostRequest postRequest){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(!postRequest.getTagsName().isEmpty()) {
            postRequest.getTagsName().forEach(tagName -> {
                var tagRequest = tagMapper.toRequest(tagName, post.getId());

                var tagExist = tagService.getByNameAndPostId(tagName, postId);
                if(tagExist != null) {
                    tagService.update(tagRequest);
                } else {
                    tagService.create(tagRequest, postMapper.modelToEntity(post));
                }
            });
        }

        if(!postRequest.getAttachmentUrl().isBlank() && !postRequest.getAttachmentDescription().isBlank()){
            var attachmentRequest = attachmentMapper.toRequest(
                    postRequest.getAttachmentUrl(),
                    postRequest.getAttachmentDescription(),
                    postRequest.getUserId());

            var attachmentExist = attachmentService.getByUrlAndPostId(postRequest.getAttachmentUrl(), postId);
            if(attachmentExist != null) {
                attachmentService.update(attachmentRequest);
            } else {
                attachmentService.create(attachmentRequest, postMapper.modelToEntity(post));
            }
        }

        var postUpdated = postService.update(postId, postRequest);

        return new ResponseEntity<>(PostMapper.modelToResponse(postUpdated), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getById(@PathVariable @NotNull Long postId){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(PostMapper.modelToResponse(post), HttpStatus.OK);
    }

    /**
     * @return All the posts in the BDD
     */
    @GetMapping()
    public ResponseEntity<List<PostResponse>> getAll(){
        var posts = postService.getAll()
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * @param userId : Id of the user
     * @return All user posts
     */
    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable @NotNull Long userId){
        var user = userService.getById(userId);

        if(user.isEmpty()){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var posts = postService.getAllByUser(user.get())
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<PostResponse>> getAllByTitle(@PathVariable String title){
        var postsByTitle = postService.getAllByTitle(title)
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(postsByTitle, HttpStatus.OK);
    }


    /** SELECT * FROM post WHERE id = (SELECT post_id FROM tag WHERE name = "js"); **/
    @GetMapping("/tagName/{tagName}")
    public ResponseEntity<?> getAllByTagName(@PathVariable String tagName){
        var tags = tagService.getAllTagByName(tagName);
        if(tags.isEmpty()){
            return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
        }

        var posts = postService.getAllByTag(tags.get());
        if(posts.isEmpty()){
            return new ResponseEntity<>("No post with this tag", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * @param tagNameList : Tag List
     * @return : Post By TagList
     */
    @GetMapping("/tagsName")
    public ResponseEntity<?> getAllByTags(@RequestBody List<String> tagNameList){
        var tags = tagService.getAllByTagNameList(tagNameList);

        var posts = postService.getAllByTags(tags);
        if(posts.isEmpty()){
            return new ResponseEntity<>("No post with this tags", HttpStatus.NOT_FOUND);
        }

        var postResponses = posts
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    /**
     * @apiNote BAD_REQUEST If the user only puts the tagName in the filter
     *                                          (there is a endpoint made for the getAllByTagName)
     * @param filters : PostFilterRequest(content, tagName, creationDate)
     * @return Posts whose match with the filter
     */
    @GetMapping("/filters")
    public ResponseEntity<List<PostResponse>> getPostsWithFilter(@RequestBody PostFilterRequest filters){

        if(filters.getTitle().isBlank() && filters.getContent().isBlank() && filters.getCreationDate().isBlank()
                && !filters.getTagName().isBlank()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var postsResponses = postService.getAllWithFilter(filters)
                                                            .stream()
                                                            .map(PostMapper::modelToResponse)
                                                            .collect(toList());
        return new ResponseEntity<>(postsResponses, HttpStatus.OK);
    }


    /**
     * @apiNote SELECT * FROM user  WHERE id = ( SELECT user_id FROM user_like WHERE post_id = id du post )
     * @param postId : Id of the user
     * @return Users who liked the post
     */
    @GetMapping("/{postId}/userLiked")
    public ResponseEntity<?> getUsersLiked(@PathVariable Long postId){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var postLikeList = likeService.getAllByPostId(postId);
        if(postLikeList.isEmpty()){
            return new ResponseEntity<>("The post has no likes", HttpStatus.NO_CONTENT);
        }

        var userLiked = postService.getUserLiked(postLikeList.get());
        return new ResponseEntity<>(userLiked, HttpStatus.OK);
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
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var likeList = likeService.getAllByUserId(userId);
        if(likeList.isPresent() && likeList.get().isEmpty()){
            return new ResponseEntity<>("The user didn't like any post", HttpStatus.NO_CONTENT);
        }

        var postLiked = postService.getPostLiked(likeList.get());

        return new ResponseEntity<>(postLiked, HttpStatus.OK);
    }


    /**
     * @apiNote SELECT id FROM post WHERE id IN (select answer_id from comment where post_id = 40);
     * @param postId : Id of the post
     * @return All post answers
     * */
    @GetMapping("{postId}/answers")
    public ResponseEntity<?> getAllPostAnswers(@PathVariable Long postId){
        var post = postService.getById(postId);
        if(post == null)  return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);

        var answers = postService.getAllPostAnswersById(postId)
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    /**
     * @param userId : Id of the user
     * @return All User Answers
     */
    @GetMapping("user/{userId}/answers")
    public ResponseEntity<?> getAllUserAnswers(@PathVariable Long userId){
        var user = userService.getById(userId);
        if(user.isEmpty()) return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        var userAnswers = postService.getAllUserAnswers(userId)
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());

        return new ResponseEntity<>(userAnswers, HttpStatus.OK);
    }


    /**
     * @param userId : Id of the user who want to see the posts of his subscriptions
     * @return The posts of the user's subscriptions
     */
    @GetMapping("/subscriptions/userId/{userId}")
    public ResponseEntity<?> getAllSubscriptionsPost(@PathVariable Long userId){

        //TODO : Faire l'appel au micro-service de groupe pour avoir les followers
        //          voir exemple -> https://github.dev/only2dhir/spring-cloud-feign-example

        var user = userService.getById(userId);
        if(user.isEmpty()) return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        var subscriptionsPost = postService.getAllSubscriptionPostByIdUser(userId)
                .stream()
                .map(PostMapper::modelToResponse)
                .collect(toList());
        return new ResponseEntity<>(subscriptionsPost, HttpStatus.OK);
    }

    /**
     * @apiNote Côté front, appeler la route de création de post PUIS la route comment
     *          Pour commenter un commentaires -> inverser les id dans le commentRequest
     * @param commentRequest : Object with Id of the post question and Id of the answer post
     */
    @PostMapping("/answer")
    public ResponseEntity<?> comment(@RequestBody @NotNull CommentRequest commentRequest){
        var user = userService.getById(commentRequest.getUserId());
        if(user.isEmpty()) return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        var post = postService.getById(commentRequest.getPostId());
        if(post == null)  return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);

        var answer = postService.getById(commentRequest.getAnswerId());
        if(answer == null) return new ResponseEntity<>("Answer post not found", HttpStatus.NOT_FOUND);


        var comment = commentService.create(postMapper.modelToEntity(post), postMapper.modelToEntity(answer), user.get());

        return new ResponseEntity<>(commentMapper.entityToResponse(comment), HttpStatus.ACCEPTED);
    }

    /**
     * @param postId : Id of the post
     * @param userId : Id of the user who likes
     */
    @PostMapping("/{postId}/like/userId/{userId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var alreadyLiked = likeService.getByPostAndUser(postMapper.modelToEntity(post), user.get());
        if(alreadyLiked != null){
            return new ResponseEntity<>("You have already like this post", HttpStatus.FORBIDDEN);
        }

        likeService.like(postMapper.modelToEntity(post), user.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * @param postId :Id of the post
     * @param userId : Id of the user who likes
     */
    @PostMapping("/{postId}/dislike/userId/{userId}")
    public ResponseEntity<?> dislike(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var user = userService.getById(userId);
        if(user.isEmpty()){
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        likeService.dislike(postMapper.modelToEntity(post), user.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @DeleteMapping("{postId}")
    public ResponseEntity<?> delete (@PathVariable Long postId){
        var post = postService.getById(postId);
        if(post == null){
            return new ResponseEntity<>(POST_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        postService.delete(postMapper.modelToEntity(post));

        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }


}
