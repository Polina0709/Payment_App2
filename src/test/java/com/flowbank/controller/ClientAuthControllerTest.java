package com.flowbank.controller;

import com.flowbank.dto.ClientLoginRequestDTO;
import com.flowbank.entity.User;
import com.flowbank.service.UserService;
import com.flowbank.config.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientAuthControllerTest {

    @Test
    void testLoginSuccess() {
        UserService userService = mock(UserService.class);
        JwtUtils jwtUtils = mock(JwtUtils.class);
        ClientAuthController controller = new ClientAuthController(userService, jwtUtils);

        ClientLoginRequestDTO request = new ClientLoginRequestDTO();
        request.setFullName("John Doe");
        request.setPassword("pass123");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("John Doe");
        user.setPassword("pass123");

        when(userService.findByFullName("John Doe")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(any(), eq("CLIENT"))).thenReturn("mocked-token");

        ResponseEntity<?> response = controller.login(request);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("mocked-token"));
    }

    @Test
    void testLoginFailure() {
        UserService userService = mock(UserService.class);
        JwtUtils jwtUtils = mock(JwtUtils.class);
        ClientAuthController controller = new ClientAuthController(userService, jwtUtils);

        ClientLoginRequestDTO request = new ClientLoginRequestDTO();
        request.setFullName("Jane");
        request.setPassword("wrong");

        when(userService.findByFullName("Jane")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.login(request);
        assertEquals(401, response.getStatusCodeValue());
    }
}
