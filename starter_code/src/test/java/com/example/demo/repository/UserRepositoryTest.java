package com.example.demo.repository;

import com.example.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext
public class UserRepositoryTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach(){
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        this.userRepository.save(user);
    }

    @Test
    public void verify_that_a_user_can_be_retrieved_by_username(){
        User user = this.userRepository.findByUsername(USERNAME);

        assertNotNull(user);
    }
}
