package com.example.postservice.data.repository;


import com.example.postservice.data.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<String, PostEntity> {


}
