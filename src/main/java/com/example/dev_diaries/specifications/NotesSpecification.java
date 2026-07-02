package com.example.dev_diaries.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.User;

import jakarta.persistence.criteria.Join;

public class NotesSpecification {
    public static Specification<Note> belongsToUser(User user){
        return (root, query, criteraiBuilder) -> {
            if(user == null) return null;
            return criteraiBuilder.equal(root.get("user"), user);
        };
    }

    public static Specification<Note> containsKeyword(String keyword){
        return (root, query, criteraiBuilder) -> {
            if(keyword == null || keyword.isBlank()) return null;

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return criteraiBuilder.or(criteraiBuilder.like(criteraiBuilder.lower(root.get("title")), likePattern),
            criteraiBuilder.like(criteraiBuilder.lower(root.get("content")), likePattern)
            );
        };
    }

    public static Specification<Note> hasTag(String tagName){
        return (root, query, criteraiBuilder) -> {
            if(tagName == null || tagName.isBlank()) return null;

            Join<Object, Object> tagsJoin = root.join("tags");

            return criteraiBuilder.equal(criteraiBuilder.lower(tagsJoin.get("name")), tagName.toLowerCase());
        };
    }

    public static Specification<Note> hasFormat(Format format){
        return (root, query, criteraiBuilder) -> {
            if(format == null) return null;
            return criteraiBuilder.equal(root.get("format"), format);
        };
    }
}
