package com.example.postservice.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Extractor {
    public String getLanguageFromCodeSection(String content){

        Pattern p = Pattern.compile("`(.+?)`", Pattern.DOTALL);
        Matcher m = p.matcher(content);
        m.find();

        return m.group(1);
    }


    public ArrayList<String> getCodeSectionFromContent(String content){
        //TODO : '#(.+?)#(.+?)#(.+?)#'
        //  #js# code1 #js#

        Pattern p = Pattern.compile("#(.+?)##", Pattern.DOTALL);
        Matcher m = p.matcher(content);

        var codes = new ArrayList<String>();

        while (m.find()){
            System.out.println("match : " + m.group(1));
            codes.add(m.group(1));
        }
        codes.forEach(code -> {
            System.out.println("code From Array List->" + code);
        });

        return codes;
    }

    public int countMatches(String str) {
        int matches = 0;
        Pattern pattern = Pattern.compile("#(.+?)##", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            matches++;
        }
        return matches;
    }
}