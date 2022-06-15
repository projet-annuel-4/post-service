package com.example.postservice.service;

import com.example.postservice.data.entities.PostEntity;
import com.example.postservice.data.entities.UserEntity;
import com.example.postservice.data.request.CodeRequest;
import com.example.postservice.domain.mapper.CodeMapper;
import org.springframework.stereotype.Service;

import com.example.postservice.data.entities.CodeEntity;
import com.example.postservice.data.repository.CodeRepository;
import com.example.postservice.domain.Extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodeService {

    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper;
    private final Extractor extractor;

    public CodeService(CodeRepository codeRepository, CodeMapper codeMapper, Extractor extractor){
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.extractor = extractor;
    }


    public Map<String, CodeEntity> create(String content, PostEntity post){

        Map<String, CodeEntity> list = new HashMap<>();

        //Splitter les codes s'il y en a plusieurs
        ArrayList<String> codeSections = extractor.getCodeSectionFromContent(content);
        if(!codeSections.isEmpty()) {
            //Pour chaque code,  enregistrer l'entité en base
            codeSections.forEach(codeSection -> {
                String language;
                String code;

                try {
                    language = extractor.getLanguageFromCodeSection(codeSection);

                    //Suppression du langage pour l'enregistrement en base
                    code = codeSection.replaceAll("`" + language + "`", "");
                    var codeCreate = codeRepository.save(codeMapper.toEntity(language, code, post));

                    list.put(language, codeCreate);

                }catch (Exception e){
                    System.out.println(e);
                }
            });
/*
            // Ajout de l'id de l'entité dans le code
            list.forEach((language, code) -> {
                System.out.println("code : " + code.getCode());
                code.setCode(code.getId().toString() + code.getCode());
                System.out.println("après code : " + code.getCode());
            });
*/
        }
        return list;
    }


}
