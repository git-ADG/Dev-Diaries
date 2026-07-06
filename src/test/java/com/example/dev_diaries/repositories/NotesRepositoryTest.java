package com.example.dev_diaries.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class NotesRepositoryTest {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Note a = new Note();
        a.setTitle("Note A");
        a.setContent("Content A");
        a.setUser(user);

        Note b = new Note();
        b.setTitle("Note B");
        b.setContent("Content B");
        b.setUser(user);

        notesRepository.save(a);
        notesRepository.save(b);

        List<Note> found = notesRepository.findByUser(user);
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(n -> "Note A".equals(n.getTitle())));
        assertTrue(found.stream().anyMatch(n -> "Note B".equals(n.getTitle())));
    }

    @Test
    void testFindByUser_Id() {
        User user = new User();
        user.setEmail("user2@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Note a = new Note();
        a.setTitle("Alpha");
        a.setContent("Alpha content");
        a.setUser(user);

        notesRepository.save(a);

        List<Note> found = notesRepository.findByUser_Id(user.getId());
        assertEquals(1, found.size());
        assertEquals("Alpha", found.get(0).getTitle());
    }
}
