package com.example.postservice.domain.mapper;

import com.example.postservice.data.entities.CodeEntity;
import com.example.postservice.data.entities.PostEntity;
import org.springframework.stereotype.Component;


@Component
public class CodeMapper {

    public CodeEntity toEntity(String language, String code, PostEntity post){
        return new CodeEntity()
                .setLanguage(language)
                .setCode(code)
                .setRunnable(null)
                .setPost(post);
    }
}
