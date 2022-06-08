package com.example.postservice.service;

import com.example.postservice.data.entities.LikeEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.LikeRepository;
import com.example.postservice.domain.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final PostMapper postMapper;

    public LikeService(LikeRepository likeRepository, PostService postService, PostMapper postMapper) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    public LikeEntity getByPostAndUser(PostEntity post, UserEntity user){
        return likeRepository.findByPostAndUser(post, user);
    }


    public void like(PostEntity post, UserEntity user){
        var like = new LikeEntity()
                .setPost(post)
                .setUser(user);
        likeRepository.save(like);

        postService.update(post.getId(), postMapper.entityToRequest(post));
    }


    @Transactional
    public void dislike(PostEntity post, UserEntity user){
        likeRepository.deleteByPostAndUser(post, user);
    }






}
