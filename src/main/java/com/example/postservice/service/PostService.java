package com.example.postservice.service;

import com.example.postservice.data.entities.*;
import com.example.postservice.data.repository.*;
import com.example.postservice.data.request.PostFilterRequest;
import com.example.postservice.data.request.PostRequest;
import com.example.postservice.domain.mapper.AttachmentMapper;
import com.example.postservice.domain.mapper.PostMapper;
import com.example.postservice.domain.mapper.SpecificationMapper;
import com.example.postservice.domain.mapper.TagMapper;
import com.example.postservice.domain.model.PostModel;
import com.example.postservice.domain.model.SearchFilter;
import com.example.postservice.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private final CommentService commentService;
    private final FollowerService followerService;
    private final CodeService codeService;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, AttachmentService attachmentService, TagRepository tagRepository, TagService tagService, TagMapper tagMapper, CommentRepository commentRepository, CommentService commentService, FollowerService followerService, CodeService codeService) {
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
        this.commentService = commentService;
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

        if(!postRequest.getTagsName().isEmpty()) {
            if(postRequest.getTagsName().size() > 5) throw new RuntimeException("5 tags Max");
            postRequest.getTagsName().forEach(tagName -> {
                var tagRequest = tagMapper.toRequest(tagName, post.getId());
                tagService.create(tagRequest, post);
            });

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


        var postTags = tagRepository.findTagEntitiesByPostId(postToUpdate.get().getId()).get()
                .stream()
                .map(TagMapper::toResponse)
                .collect(toList());

        var postModel = PostMapper.entityToModel(postToUpdate.get());
        postModel.setTags(postTags);

        return postModel;
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

    public List<PostModel> getAllByCreationDate(LocalDateTime creationDate){
        return postRepository.findAllByCreationDateAfter(creationDate)
                .stream()
                .map(PostMapper::entityToModel)
                .collect(toList());
    }

    public List<PostModel> getAllByContent(String content){
        return postRepository.findAllByContent(content)
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


    public List<PostModel> getAllWithFilterV1(PostFilterRequest filters){

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

    public List<PostModel> getAllWithFilterV2(PostFilterRequest filters){
        var dateForQuery = LocalDateTime.now();

        if (!filters.getCreationDate().isEmpty() && filters.getCreationDate() != null) {
            if(DateTimeUtil.isValid(filters.getCreationDate())) {
                dateForQuery = DateTimeUtil.dateFromString(filters.getCreationDate());
            }
        } else {
            dateForQuery = DateTimeUtil.dateFromString("1900-01-01 00:00:00");
        }


        List<PostModel> postFound;

        if(!filters.getTitle().isEmpty() && !filters.getContent().isEmpty() && !filters.getTagName().isEmpty()){

            postFound = postRepository.findAllByAllFilters(filters.getTitle(), filters.getContent(), dateForQuery)
                    .stream()
                    .map(PostMapper::entityToModel)
                    .collect(toList());
        } else {

            postFound = postRepository.findAllByAnyFilters(filters.getTitle(), filters.getContent(), dateForQuery)
                    .stream()
                    .map(PostMapper::entityToModel)
                    .collect(toList());
        }

        return addTagsToPostModelList(postFound);
    }

    //public HashSet<PostModel> getAllWithFilterV3(PostFilterRequest filters){
    public ArrayList<PostModel> getAllWithFilterV3(PostFilterRequest filters){
        var postFound = new ArrayList<PostModel>();

        var union = new ArrayList<ArrayList<PostModel>>();

        if(!filters.getTitle().isEmpty() && filters.getTitle() != null){
            postFound.addAll(getAllByTitle(filters.getTitle()));

            union.add((ArrayList<PostModel>) getAllByTitle(filters.getTitle()));
        }

        if(!filters.getContent().isEmpty() && filters.getContent() != null){
            postFound.addAll(getAllByContent(filters.getContent()));

            union.add((ArrayList<PostModel>) getAllByContent(filters.getContent()));
        }

        var postByTagsForTest3 = new ArrayList<PostModel>();

        if(!filters.getTagName().isEmpty() && filters.getTagName() != null){
            var tags = tagRepository.findTagEntitiesByName(filters.getTagName());
            if(tags.isPresent()){
                var postsByTag = this.getAllByTag(tags.get());
                postsByTag.forEach(postEntity -> {
                    if(postEntity != null) {
                        postFound.add(postEntity);

                        postByTagsForTest3.add(postEntity);
                    }
                });
            }
        }
        union.add(postByTagsForTest3);

        var dateForQuery = LocalDateTime.now();
        if(!filters.getCreationDate().isEmpty() && filters.getCreationDate() != null){
            if(DateTimeUtil.isValid(filters.getCreationDate())) {
                dateForQuery = DateTimeUtil.dateFromString(filters.getCreationDate());
            }
            postFound.addAll(getAllByCreationDate(dateForQuery));

            union.add((ArrayList<PostModel>) getAllByCreationDate(dateForQuery));
        }

        //return Sets.newHashSet(postFound);

        return searchIntersection(new ArrayList<>(union));
    }

    public List<PostModel> getAllWithFilterV4(PostFilterRequest filters){
        Predicate<PostEntity> postByTitle = e -> filters.getTitle().isEmpty()?true:Objects.equals(e.getTitle(),filters.getTitle());
        Predicate<PostEntity> postByContent = e -> filters.getContent().isEmpty()?true:Objects.equals(e.getContent(),filters.getContent());

        //var tagName = tagRepository.findTagEntitiesByName(filters.getTagName());
        //Predicate<PostEntity> postByTagName = e -> filters.getTagName().isEmpty()?true:Objects.equals(e.get,filters.getTagName());
        Predicate<PostEntity> postByCreationDate = e -> filters.getCreationDate().isEmpty()?true:Objects.equals(e.getCreationDate(),filters.getCreationDate());


        var postFound = postRepository.findAll()
                .stream()
                //.filter(postByTitle.and(postByContent).and(postByTagName).and(postByCreationDate))
                .filter(postByTitle.and(postByContent).and(postByCreationDate))
                .map(PostMapper::entityToModel)
                .collect(toList());

        return addTagsToPostModelList(postFound);
    }

    /**
     * Intersection entre les tableau de chaque filter
     * @param tabs
     */
    private ArrayList<PostModel> searchIntersection(ArrayList<ArrayList<PostModel>> tabs){
        var res = new ArrayList<PostModel>(tabs.get(0));

        for (List<PostModel> tab : tabs) {
            for (PostModel post: tab){
                if(res.contains(post)){
                    res.add(post);
                }
            }
        }

        res.forEach(r -> System.out.println(r.getId()));
        return res;
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

    public List<PostModel> getAllUserAnswers(Long userId){
        var comments = commentRepository.findAllByUserId(userId);

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

        return sortPostByLocalDateTime(posts);
    }

    @Transactional
    public void delete(PostEntity post){
        attachmentRepository.deleteAllByPostId(post.getId());
        codeService.deleteAllByPostId(post.getId());
        commentService.deleteAllByPostId(post);
        tagService.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());

        post.setUser(null);
        postRepository.save(post);

        postRepository.deleteById(post.getId());
    }


    private List<PostModel> sortPostByLocalDateTime(List<PostModel> posts){
        Comparator<PostModel> comparator = Comparator.comparing(PostModel::getCreationDate);

        posts.sort(comparator);

        return posts;
    }

    public List<PostModel> searchByFilters(List<SearchFilter> filters){
        List<SearchFilter> postQueryFilters = new ArrayList<>();
        List<SearchFilter> filteredFilters = new ArrayList<>();
        List<String> postQueryFiltersField = Arrays.asList("tags");
        for (SearchFilter filter : filters){
            if( !Collections.disjoint(Collections.singletonList(filter.getField()), postQueryFiltersField)){
                postQueryFilters.add(filter);
            } else {
                filteredFilters.add(filter);
            }
        }
        List<PostEntity> postEntities = new ArrayList<>();
        if( filteredFilters.size() > 0) {
            postEntities = postRepository.findAll(SpecificationMapper.FromSearchFilterToSpecification(filteredFilters));
        }
        if( postQueryFilters.size() == 0){
            return postEntities.stream().map(PostMapper::entityToModel).collect(Collectors.toList());
        }
        if( postEntities.size() == 0){
            postEntities = postRepository.findAll();
        }
        List<PostEntity> postEntitiesPostFiltered = getPostEntityByApplyingPostFilters(postQueryFilters, postEntities);


        var postModel =  postEntitiesPostFiltered
                .stream()
                .map(PostMapper::entityToModel)
                .collect(Collectors.toList());

        return addTagsToPostModelList(postModel);
    }

    private List<PostEntity> getPostEntityByApplyingPostFilters(List<SearchFilter> filters, List<PostEntity> postEntities){
        List<PostEntity> filteredPostEntities = new ArrayList<>();
        for (int j = 0; j< filters.size(); j++){
            for (int i = 0 ; i < postEntities.size(); i++){
                if (filters.get(j).getField().equals("tags") ) {
                    List<String> tagName = tagService.getAllTagNameByPostId(postEntities.get(i).getId());
                    if(tagName == null){
                        continue;
                    }
                    int size = tagName.size();
                    tagName.removeAll(filters.get(j).getValues());
                    if ( size != tagName.size() && size != 0){
                        filteredPostEntities.add(postEntities.get(i));
                    }
                }
            }
            if( j == filters.size()-1 ){
                break;
            }
            postEntities = new ArrayList<>(filteredPostEntities);
            filteredPostEntities = new ArrayList<>();
        }
        return filteredPostEntities;
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
