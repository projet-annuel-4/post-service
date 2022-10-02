package com.example.postservice.service;

import com.example.postservice.data.entities.CommentEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.repository.CommentRepository;
import com.example.postservice.domain.mapper.PostMapper;
import com.example.postservice.domain.model.PostModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentEntity create(PostEntity post, PostEntity answer, UserEntity user){
        var comment = new CommentEntity()
                .setPost(post)
                .setAnswer(answer)
                .setUser(user);
        return commentRepository.save(comment);
    }

    public CommentEntity getById(PostEntity postEntity){
        return commentRepository.getById(postEntity.getId());
    }

    public PostModel getPostOfTheAnswer(Long postId){
        var commentEntity =  commentRepository.findCommentEntityByAnswerId(postId);
        return PostMapper.entityToModel(commentEntity.getPost());
    }

    public void deleteByAnswerId(PostEntity postEntity){
        commentRepository.deleteAllByAnswerId(postEntity.getId());
    }

    public void deleteAllByPostId(PostEntity postEntity){
        commentRepository.deleteAllByPost(postEntity);
    }



}
