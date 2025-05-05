package com.flowbank.controller;

import com.flowbank.config.JwtUtils;
import com.flowbank.dto.ClientLoginRequestDTO;
import com.flowbank.dto.JwtResponseDTO;
import com.flowbank.entity.User;
import com.flowbank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientAuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ClientLoginRequestDTO request) {
        Optional<User> userOpt = userService.findByFullName(request.getFullName());

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            log.warn("Failed login attempt for full name: {}", request.getFullName());
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtils.generateToken(userOpt.get().getId().toString(), "CLIENT");
        log.info("Client {} logged in successfully", userOpt.get().getFullName());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
