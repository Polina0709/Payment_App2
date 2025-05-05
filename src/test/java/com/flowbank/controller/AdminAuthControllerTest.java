package com.flowbank.controller;

import com.flowbank.dto.AdminLoginRequestDTO;
import com.flowbank.dto.JwtResponseDTO;
import com.flowbank.entity.Admin;
import com.flowbank.service.AdminService;
import com.flowbank.config.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminAuthControllerTest {

    private final AdminService adminService = mock(AdminService.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final AdminAuthController controller = new AdminAuthController(adminService, jwtUtils);

    @Test
    void loginSuccess() {
        AdminLoginRequestDTO request = new AdminLoginRequestDTO();
        request.setUsername("admin");
        request.setPassword("admin123");

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setPassword("admin123");

        when(adminService.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(jwtUtils.generateToken(admin.getId().toString(), "ADMIN")).thenReturn("admin-token");

        ResponseEntity<?> response = controller.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(JwtResponseDTO.class, response.getBody());
        assertEquals("admin-token", ((JwtResponseDTO) response.getBody()).getToken());
    }

    @Test
    void loginWrongPassword() {
        AdminLoginRequestDTO request = new AdminLoginRequestDTO();
        request.setUsername("admin");
        request.setPassword("wrong");

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setPassword("correct");

        when(adminService.findByUsername("admin")).thenReturn(Optional.of(admin));

        ResponseEntity<?> response = controller.login(request);
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void loginAdminNotFound() {
        AdminLoginRequestDTO request = new AdminLoginRequestDTO();
        request.setUsername("unknown");
        request.setPassword("123");

        when(adminService.findByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.login(request);
        assertEquals(401, response.getStatusCodeValue());
    }
}
