package com.serg.ans.cryptocurrencywatcher.service;

import com.serg.ans.cryptocurrencywatcher.entity.User;
import com.serg.ans.cryptocurrencywatcher.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private static User testUser;

    @BeforeAll
    static void initEntities() {
        testUser = User.builder().id(1).username("admin").build();
    }

    @Test
    void returnEmptyOptionalIfUsernameEmpty() {
        assertTrue(userService.findByUsername("").isEmpty());
    }

    @Test
    void returnEmptyOptionalIfUsernameContainsOnlyWhitespaces() {
        assertTrue(userService.findByUsername("  ").isEmpty());
    }

    @Test
    void returnEmptyOptionalIfUsernameNull() {
        assertTrue(userService.findByUsername(null).isEmpty());
    }

    @Test
    void returnedEntityEqualsEntityFromRepository() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        assertEquals(testUser, userService.findByUsername("testUser").get());
    }

    @Test
    void returnEmptyOptionalIfEntityNotExistsInRepository() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        assertTrue(userService.findByUsername("testUser").isEmpty());
    }
}