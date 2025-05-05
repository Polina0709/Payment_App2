package com.flowbank.service;

import com.flowbank.entity.Log;
import com.flowbank.entity.User;
import com.flowbank.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

public class LogService {

    private final LogRepository logRepository;

    public void logAction(User user, String action) {
        Log entry = new Log();
        entry.setId(UUID.randomUUID());
        entry.setUser(user);
        entry.setAction(action);
        entry.setTimestamp(LocalDateTime.now());
        logRepository.save(entry);

        log.info("User [{}]: {}", user.getFullName(), action);
    }

    public List<Log> getUserLogs(UUID userId) {
        return logRepository.findByUserId(userId);
    }
}
