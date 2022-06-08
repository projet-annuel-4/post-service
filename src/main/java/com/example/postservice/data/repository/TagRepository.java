package com.example.postservice.data.repository;

import com.example.postservice.data.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    TagEntity getById(Long id);
    List<TagEntity> getTagEntitiesByName(String tagName);
}
