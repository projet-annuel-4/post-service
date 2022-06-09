package com.example.postservice.data.repository;

import com.example.postservice.data.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    TagEntity getById(Long id);
    TagEntity findByName(String tagName);
    TagEntity findByPostId(Long id);
    Optional<List<TagEntity>> findTagEntitiesByName(String tagName);
    TagEntity findTagEntitiesByNameAndPostId(String name, Long post_id);

    void deleteAllByPostId(Long postId);

}
