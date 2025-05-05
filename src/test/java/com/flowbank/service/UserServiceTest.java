package com.flowbank.service;

import com.flowbank.entity.User;
import com.flowbank.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    LogService logService = mock(LogService.class);
    private final UserService userService = new UserService(userRepository, logService);

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPersonalNumber("123456");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(user);

        assertNotNull(result.getId());
        assertEquals("Test User", result.getFullName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByEmail("user@example.com");

        assertTrue(found.isPresent());
        assertEquals("user@example.com", found.get().getEmail());
    }
}