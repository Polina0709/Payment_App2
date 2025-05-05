package com.flowbank.controller;

import com.flowbank.dto.UserProfileDTO;
import com.flowbank.entity.User;
import com.flowbank.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    private final UserService userService = mock(UserService.class);
    private final ProfileController controller = new ProfileController(userService);

    @Test
    void getProfile_Success() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPersonalNumber("1234567890");

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = controller.getProfile(userId.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(UserProfileDTO.class, response.getBody());

        UserProfileDTO dto = (UserProfileDTO) response.getBody();
        assertEquals("Test User", dto.getFullName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("1234567890", dto.getPersonalNumber());
    }

    @Test
    void getProfile_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userService.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getProfile(userId.toString());

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void logout_AlwaysReturnsOk() {
        ResponseEntity<?> response = controller.logout();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully", response.getBody());
    }
}