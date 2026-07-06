package com.example.dev_diaries.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.dev_diaries.models.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("abc@abc.com");
        user.setPassword("password");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("abc@abc.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());

        Optional<User> missingUser = userRepository.findByEmail("null@null.com");

        assertThat(missingUser).isNotPresent();
    }
}
