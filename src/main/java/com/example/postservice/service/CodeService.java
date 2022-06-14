package com.example.postservice.service;

import org.springframework.stereotype.Service;

import com.example.postservice.data.entities.CodeEntity;
import com.example.postservice.data.repository.CodeRepository;
import com.example.postservice.domain.Extractor;

@Service
public class CodeService {

    private final CodeRepository codeRepository;
    private final Extractor extractor;

    public CodeService(CodeRepository codeRepository, Extractor extractor){
        this.codeRepository = codeRepository;
        this.extractor = extractor;
    }

/*
    public CodeEntity create(){
        return codeRepository.save(entity)
    }
*/
}
