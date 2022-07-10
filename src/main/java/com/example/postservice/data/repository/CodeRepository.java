package com.example.postservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.postservice.data.entities.CodeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, Long>{

    void deleteAllByPostId(Long postId);
    
}
