package com.example.postservice.data.repository;

import com.example.postservice.data.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    TagEntity getById(Long id);
}
