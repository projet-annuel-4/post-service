package com.example.postservice.data.repository;

import com.example.postservice.data.entities.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
}
