package com.example.dev_diaries.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class TaggingServiceTest {

    private final TaggingService taggingService = new TaggingService();

    @Test
    void extractTagsFromContentReturnsEmptySetForBlankContent() {
        assertThat(taggingService.extractTagsFromContent(null)).isEmpty();
        assertThat(taggingService.extractTagsFromContent("   ")).isEmpty();
    }

    @Test
    void extractTagsFromContentFindsMultiplePatternsIgnoringCase() {
        String content = "I love java and git commands while solving LeetCode problems.";

        Set<String> tags = taggingService.extractTagsFromContent(content);

        assertThat(tags).contains("Java / Spring", "Git / VCS", "Competitive Programming");
    }
}
