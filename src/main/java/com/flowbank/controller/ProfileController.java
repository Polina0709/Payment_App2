package com.flowbank.controller;

import com.flowbank.dto.UserProfileDTO;
import com.flowbank.entity.User;
import com.flowbank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal String userId) {
        User user = userService.findById(UUID.fromString(userId))
                .orElse(null);

        if (user == null) {
            log.warn("ProfileController - Failed to find user with ID {}", userId);
            return ResponseEntity.status(404).body("User not found");
        }

        log.info("ProfileController - User {} opened their profile", user.getFullName());

        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPersonalNumber(user.getPersonalNumber());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("ProfileController - Client logged out");
        return ResponseEntity.ok("Logged out successfully");
    }
}
