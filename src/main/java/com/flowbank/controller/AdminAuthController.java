package com.flowbank.controller;

import com.flowbank.dto.AdminLoginRequestDTO;
import com.flowbank.dto.JwtResponseDTO;
import com.flowbank.entity.Admin;
import com.flowbank.service.AdminService;
import com.flowbank.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequestDTO request) {
        Optional<Admin> adminOpt = adminService.findByUsername(request.getUsername());

        if (adminOpt.isEmpty() || !adminOpt.get().getPassword().equals(request.getPassword())) {
            log.warn("Failed login attempt for username '{}'", request.getUsername());
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtils.generateToken(adminOpt.get().getId().toString(), "ADMIN");
        log.info("Admin '{}' logged in successfully", request.getUsername());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
