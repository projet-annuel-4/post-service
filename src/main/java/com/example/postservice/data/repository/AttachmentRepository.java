package com.example.postservice.data.repository;

import com.example.postservice.data.entities.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

    AttachmentEntity findByUrlAndPostId(String url, Long post_id);
    AttachmentEntity findByPostId(Long id);
    void deleteAllByPostId(Long postId);
}
