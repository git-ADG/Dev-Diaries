package com.example.dev_diaries.repositories;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.dev_diaries.models.Tag;

@DataJpaTest
public class TagsRepositoryTest {
    @Autowired
    private TagsRepository tagsRepository;

    @Test
    void testFindByNameIgnoreCase() {
        Tag tag = new Tag();
        tag.setName("javAsCript");

        tagsRepository.save(tag);

        Optional<Tag> foundTag = tagsRepository.findByNameIgnoreCase("javascript");

        Optional<Tag> missingTag = tagsRepository.findByNameIgnoreCase("java");
        
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo("javAsCript");

        assertThat(missingTag).isNotPresent();

        // assertThat(foundTag).isEqualTo(tag);
    }
}
