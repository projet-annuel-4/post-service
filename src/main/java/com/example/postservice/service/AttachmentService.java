package com.example.postservice.service;

import com.example.postservice.data.entities.AttachmentEntity;
import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.repository.AttachmentRepository;
import com.example.postservice.data.request.AttachmentRequest;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public AttachmentEntity create(AttachmentRequest attachmentRequest, PostEntity post){
        var attachment = new AttachmentEntity()
                .setUrl(attachmentRequest.getUrl())
                .setDescription(attachmentRequest.getDescription())
                .setPost(post);

        return attachmentRepository.save(attachment);
    }

    public AttachmentEntity update(AttachmentRequest attachmentRequest){
        var attachment = attachmentRepository.findByUrlAndPostId(attachmentRequest.getUrl(),
                                                                                attachmentRequest.getPostId())
                .setUrl(attachmentRequest.getUrl())
                .setDescription(attachmentRequest.getDescription());

        return attachmentRepository.save(attachment);
    }

    public AttachmentEntity getByUrlAndPostId(String url, Long postId){
        return attachmentRepository.findByUrlAndPostId(url, postId);
    }


}
