package com.example.postservice.data.repository;


import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    PostEntity getById(Long id);
    List<PostEntity> getAllByUser(UserEntity user);

    @Query("select post from PostEntity post where post.title like %:title%")
    List<PostEntity> getAllByTitle(@Param("title") String title);
    @Query("select post from PostEntity post where post.content like %:content%")
    List<PostEntity> findAllByContent(@Param("content") String content);
    @Query("select post from PostEntity post where post.creationDate > :creationDate")
    List<PostEntity> findAllByCreationDateAfter(@Param("creationDate") LocalDateTime creationDate);
    List<PostEntity> findAllById(Long id);

    @Query("select post from PostEntity post where post.content like %:content% and post.creationDate > :creationDate")
    List<PostEntity> findAllByContentOrUpdateDate(@Param("content") String content, @Param("creationDate") LocalDateTime creationDate);

    @Query("SELECT post " +
            "FROM PostEntity post " +
            "WHERE post.title like %:title% " +
            "AND post.content like %:content% " +
            "AND post.creationDate > :creationDate")
    List<PostEntity> findAllByAllFilters(@Param("title") String title, @Param("content") String content, @Param("creationDate") LocalDateTime creationDate);

    @Query("SELECT post " +
            "FROM PostEntity post " +
            "WHERE post.title like %:title% " +
            "OR post.content like %:content% " +
            "OR post.creationDate > :creationDate")
    List<PostEntity> findAllByAnyFilters(@Param("title") String title, @Param("content") String content, @Param("creationDate") LocalDateTime creationDate);



    /*
           KO : retourne qu'une seule valeur
    @Query("SELECT post FROM PostEntity post WHERE post.id IN :commentIds")
    List<PostEntity> find_all_answers_in_commentEntity_list(@Param("commentIds") ArrayList<Long> commentIds);
    */
}
