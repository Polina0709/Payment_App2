package com.flowbank.service;

import com.flowbank.entity.Log;
import com.flowbank.entity.User;
import com.flowbank.repository.LogRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LogServiceTest {

    private final LogRepository logRepository = mock(LogRepository.class);
    private final LogService logService = new LogService(logRepository);

    @Test
    void testLogAction() {
        User user = new User();
        user.setId(UUID.randomUUID());
        String action = "LOGIN";

        logService.logAction(user, action);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testGetUserLogs() {
        UUID userId = UUID.randomUUID();
        List<Log> logs = Arrays.asList(new Log(), new Log());

        when(logRepository.findByUserId(userId)).thenReturn(logs);

        List<Log> result = logService.getUserLogs(userId);

        assertEquals(2, result.size());
        verify(logRepository, times(1)).findByUserId(userId);
    }
}