package com.example.dev_diaries.services;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TaggingService {

    private final Map<Pattern, String> tagDictionary = new LinkedHashMap<>();

    public TaggingService(){
        tagDictionary.put(Pattern.compile("(?i)\\b(sudo|apt-get|bash|linux)\\b"), "Linux");
        tagDictionary.put(Pattern.compile("(?i)\\b(docker|container|compose)\\b"), "Docker");
        tagDictionary.put(Pattern.compile("(?i)\\b(react|useState|useEffect)\\b"), "React");
        tagDictionary.put(Pattern.compile("(?i)\\b(spring boot|jpa|hibernate)\\b"), "Spring Boot");
    }

    public Set<String> extractTagsFromContent(String content){
        if(content == null || content.isBlank()){
            return Collections.emptySet();
        }

        Set<String> detectedTags = tagDictionary.entrySet().stream()
                                    .filter((entry) -> entry.getKey().matcher(content).find())
                                    .map(Map.Entry::getValue)
                                    .collect(Collectors.toSet());
        return detectedTags;
    }
}
